package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.TutorBidRequest;
import com.grabtutor.grabtutor.dto.response.TutorBidResponse;
import com.grabtutor.grabtutor.entity.ChatRoom;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.entity.UserTransaction;
import com.grabtutor.grabtutor.enums.BiddingStatus;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.TutorBidMapper;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.TutorBidService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TutorBidServiceImpl implements TutorBidService {
    PostRepository postRepository;
    TutorBidRepository tutorBidRepository;
    UserTransactionRepository userTransactionRepository;
    ChatRoomRepository chatRoomRepository;
    UserRepository userRepository;
    TutorBidMapper tutorBidMapper;
    @Override
    public TutorBidResponse addTutorBid(TutorBidRequest request) {
        var bid = tutorBidMapper.toTutorBid(request);
        var post  = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        bid.setPost(post);
        postRepository.save(post);
        return tutorBidMapper.toTutorBidResponse(bid);
    }

    @Override
    public List<TutorBidResponse> getAllTutorBid(String postId) {
        return tutorBidRepository.findByPostId(postId)
                .stream().map(tutorBidMapper::toTutorBidResponse).toList();
    }

    @Override
    @Transactional
    public void acceptTutor(String tutorBidId) {
        var bid =  tutorBidRepository.findById(tutorBidId)
                .orElseThrow(()-> new AppException(ErrorCode.TUTOR_BID_NOT_FOUND));
        var post = bid.getPost();
        post.setAccepted(true);
        post.getTutorBids()
                .forEach(tb -> {
                    tb.setStatus(BiddingStatus.REJECTED);
                    if(tb.getId().equals(tutorBidId)) {
                        tb.setStatus(BiddingStatus.ACCEPTED);
                    }
                });
        var sender = post.getUser();
        var receiver = bid.getUser();
        List<User> users = new ArrayList<User>();
        users.add(sender);
        users.add(receiver);

        ChatRoom room = ChatRoom.builder()
                .post(post)
                .users(users)
                .build();

        var transaction = UserTransaction.builder()
                .post(post)
                .sender(sender)
                .receiver(receiver)
                .amount(bid.getProposedPrice())
                .build();
        postRepository.save(post);
        chatRoomRepository.save(room);
        userTransactionRepository.save(transaction);
    }
}
