package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.AcceptPostRequest;
import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import com.grabtutor.grabtutor.entity.ChatRoom;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.Subject;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.PostMapper;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.FileUploadService;
import com.grabtutor.grabtutor.service.PostService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
    FileUploadService fileUploadService;
    SubjectRepository subjectRepository;
    ChatRoomRepository chatRoomRepository;
    AccountBalanceRepository  accountBalanceRepository;
//    RedisTemplate<String, String> redisTemplate;

    int acceptFee = 100;


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Override
    @Transactional
    public PostResponse addPost(String userId, String subjectId, PostRequest request,
                                String imageUrl) throws IOException {
        Post post = postMapper.toPost(request);
        if(imageUrl != null && !imageUrl.isEmpty())
            post.setImageUrl(imageUrl);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(user.isDeleted()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        post.setUser(user);
        Subject subject = subjectRepository
                .findById(subjectId).orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        post.setSubject(subject);

        //Logic trừ tiền
        var accountBalance = accountBalanceRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_BALANCE_NOT_FOUND));
        if(accountBalance.getBalance() < post.getReward()) {
            throw new AppException(ErrorCode.ACCOUNT_DONT_HAVE_ENOUGH_MONEY);
        }
        else {
            accountBalance.setBalance(accountBalance.getBalance() - post.getReward());
            accountBalanceRepository.save(accountBalance);
        }
        //Add vào queue -> tự động xóa bài sau 15 phút
//        redisTemplate.opsForZSet().add("post:expire", post.getId(),
//                post.getCreatedAt()
//                    .atZone(ZoneId.systemDefault())
//                    .toInstant()
//                    .toEpochMilli() + 60000*15);
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
    public PostResponse updatePost(String userId, String postId, PostRequest postRequest,
                                   String imageUrl, String subjectId) throws IOException {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));

        if(post.isDeleted()) {
            throw new AppException(ErrorCode.POST_NOT_EXIST);
        }
        if(!post.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        postMapper.updatePostFromRequest(postRequest, post);
        if(imageUrl != null && !imageUrl.isEmpty()) {
            post.setImageUrl(imageUrl);
        }
        if(subjectId != null && !subjectId.isEmpty()) {
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
            post.setSubject(subject);
        }
        if(imageUrl != null && !imageUrl.isEmpty()) {
            post.setImageUrl(imageUrl);
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
    //Tutor accept post -> bị trừ phí + add vào room chat
    @Override
    @PreAuthorize("hasRole('TUTOR')")
    @Transactional
    public void acceptPost(AcceptPostRequest request) {
        var post  = postRepository.findById(request.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaim("userId");
        var tutor = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        var postUser = post.getUser();
        List<User> users = List.of(tutor,  postUser);

        var tutorAccountBalance = accountBalanceRepository.findByUserId(userId)
                .orElseThrow(()-> new AppException(ErrorCode.ACCOUNT_BALANCE_NOT_FOUND));
        if(tutorAccountBalance.getBalance() < acceptFee) {
            throw new AppException(ErrorCode.ACCOUNT_DONT_HAVE_ENOUGH_MONEY);
        }
        else {
            tutorAccountBalance.setBalance(tutorAccountBalance.getBalance() - acceptFee);
            accountBalanceRepository.save(tutorAccountBalance);
        }
        var newRoom = ChatRoom.builder()
                .users(users)
                .build();
        chatRoomRepository.save(newRoom);

        post.setAccepted(true);
        postRepository.save(post);
    }
}
