package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.AcceptPostRequest;
import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import com.grabtutor.grabtutor.entity.*;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.PostMapper;
import com.grabtutor.grabtutor.mapper.TutorBidMapper;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.PostService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository;
    SubjectRepository subjectRepository;
    RedisTemplate<String, String> redisTemplate;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Override
    @Transactional
    public PostResponse addPost(String subjectId, PostRequest request)
            throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        Post post = postMapper.toPost(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(user.isDeleted()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        post.setUser(user);
        Subject subject = subjectRepository
                .findById(subjectId).orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        post.setSubject(subject);

        //Add vào queue -> tự động xóa bài sau 6 giờ
        //Lúc này worker sẽ phải gửi thông báo cho user -> post đã được gỡ rồi
        redisTemplate.opsForZSet().add("post:expire", post.getId(),
                post.getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli() + 3600000*6);
        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public PostResponse getPostByPostId(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));

        if(post.isDeleted()) {
            throw new AppException(ErrorCode.POST_NOT_EXIST);
        }
        return postMapper.toPostResponse(post);
    }

    @Override
    public PostResponse updatePost(String postId, PostRequest postRequest,
                                   String subjectId) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));

        if(post.isDeleted()) {
            throw new AppException(ErrorCode.POST_NOT_EXIST);
        }
        if(!post.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        postMapper.updatePostFromRequest(postRequest, post);

        if(subjectId != null && !subjectId.isEmpty()) {
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
            post.setSubject(subject);
        }

        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public void deletePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));

        if(post.isDeleted()) {
            throw new AppException(ErrorCode.POST_NOT_EXIST);
        }
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Override
    public List<PostResponse> getPostByUserId(String userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        posts.removeIf(Post::isDeleted);
        return posts.stream().map(postMapper::toPostResponse).toList();
    }

    @Override
    public PageResponse<?> getAllPosts(int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)*(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("desc")){
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
            }

        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));
        Page<Post> posts = postRepository.findAllByIsDeletedFalse(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(posts.getTotalPages())
                .items(posts.stream().map(postMapper::toPostResponse).toList())
                .build();

    }

}
