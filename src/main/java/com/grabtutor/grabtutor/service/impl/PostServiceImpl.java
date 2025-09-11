package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.PostRequest;
import com.grabtutor.grabtutor.dto.response.PostResponse;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.PostMapper;
import com.grabtutor.grabtutor.repository.PostRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.PostService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@Data
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    private final UserRepository userRepository;

    @Override
    public PostResponse addPost(PostRequest postRequest) {
        Post post = postMapper.toPost(postRequest);
        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public PostResponse getPostByPostId(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        return postMapper.toPostResponse(post);
    }

    @Override
    public PostResponse updatePost(String postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        postMapper.updatePostFromRequest(postRequest, post);
        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    @Override
    public void deletePost(String postId) {
        userRepository.deleteById(postId);
    }

    @Override
    public List<PostResponse> getPostByUserId(String userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map(postMapper::toPostResponse).toList();
    }

    @Override
    public List<PostResponse> getAllPosts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.stream().map(postMapper::toPostResponse).toList();
    }
}
