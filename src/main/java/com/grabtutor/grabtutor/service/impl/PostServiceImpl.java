package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.entity.Subject;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.PostMapper;
import com.grabtutor.grabtutor.repository.PostRepository;
import com.grabtutor.grabtutor.repository.SubjectRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.FileUploadService;
import com.grabtutor.grabtutor.service.PostService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Builder
@Data
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    private final UserRepository userRepository;
    FileUploadService fileUploadService;
    SubjectRepository subjectRepository;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Override
    public PostResponse addPost(String userId, String subjectId,String description , MultipartFile file) throws IOException {
        String imageUrl = fileUploadService.uploadFile(file);
        Post post = new Post();
        post.setDescription(description);
        post.setImageUrl(imageUrl);
        post.setUser(userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        post.setSubject(subject);
        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public PostResponse getPostByPostId(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        if(post.isDeleted()) {
            throw new AppException(ErrorCode.POST_NOT_EXIST);
        }
        return postMapper.toPostResponse(post);
    }

    @Override
    public PostResponse updatePost(String userId, String postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        if(post.isDeleted()) {
            throw new AppException(ErrorCode.POST_NOT_EXIST);
        }
        if(!post.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        postMapper.updatePostFromRequest(postRequest, post);
        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    @Override
    public void deletePost(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
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
