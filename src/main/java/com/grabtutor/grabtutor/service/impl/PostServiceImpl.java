package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import com.grabtutor.grabtutor.entity.*;
import com.grabtutor.grabtutor.enums.PostStatus;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.PostMapper;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.PostService;
import com.grabtutor.grabtutor.service.worker.ServiceJob;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    ServiceJob serviceJob;

    @PreAuthorize("hasRole('USER')")
    @Override
    @Transactional
    public PostResponse addPost(String subjectId, PostRequest request) {
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
        post.setStatus(PostStatus.OPEN);
        postRepository.save(post);
        //Add vào queue -> tự động xóa bài sau 30 phút
        //Lúc này worker sẽ phải gửi thông báo cho user -> post đã được gỡ rồi
        serviceJob.addCheckPost(post.getId(), post.getCreatedAt());

        return postMapper.toPostResponse(post);
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

    @PreAuthorize("hasRole('USER')")
    @Override
    public PostResponse updatePost(String postId, PostRequest postRequest,
                                   String subjectId){

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
    @PreAuthorize("hasRole('USER')")
    @Override
    public void deletePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));

        if(post.isDeleted()) {
            throw new AppException(ErrorCode.POST_NOT_EXIST);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        if(!post.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Override
    public PageResponse<?> getPostByUserId(String userId, int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
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
        Page<Post> posts = postRepository.findByUserId(userId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(posts.getTotalPages())
                .items(posts.stream().map(postMapper::toPostResponse).toList())
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public PageResponse<?> getPostMyPost(int pageNo, int pageSize, String... sorts) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
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
        Page<Post> posts = postRepository.findByUserId(userId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(posts.getTotalPages())
                .items(posts.stream().map(postMapper::toPostResponse).toList())
                .build();
    }


    @Override
    public PageResponse<?> getAllPosts(int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
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

    @Override
    public PageResponse<?> searchPostsByName(String keyword, int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
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
        Page<Post> posts = postRepository.searchDescription(keyword, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(posts.getTotalPages())
                .items(posts.stream().map(postMapper::toPostResponse).toList())
                .build();
    }

}
