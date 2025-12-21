package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.TutorBidRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.TutorBidResponse;
import com.grabtutor.grabtutor.entity.*;
import com.grabtutor.grabtutor.enums.BiddingStatus;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.TutorBidMapper;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.TutorBidService;
import com.grabtutor.grabtutor.service.worker.ServiceJob;
import com.grabtutor.grabtutor.socket.NotificationService;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TutorBidServiceImpl implements TutorBidService {
    PostRepository postRepository;
    TutorBidRepository tutorBidRepository;
    AccountBalanceRepository  accountBalanceRepository;
    UserRepository userRepository;
    TutorBidMapper tutorBidMapper;
    NotificationService notificationService;
    ServiceJob serviceJob;


    @Override
    @Transactional
    @PreAuthorize("hasRole('TUTOR')")
    public TutorBidResponse addTutorBid(TutorBidRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");

        var bid = tutorBidMapper.toTutorBid(request);
        var post  = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        if(post.isAccepted()) throw new AppException(ErrorCode.POST_ALREADY_ACCEPTED);
        post.getTutorBids().forEach(tb -> {
            if(tb.getUser().getId().equals(userId) && tb.getStatus().equals(BiddingStatus.PENDING)) {
                throw new AppException(ErrorCode.ALREADY_PROPOSE_BID);
            }
        });

        var sender = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        post.getTutorBids().add(bid);
        bid.setPost(post);

        bid.setUser(sender);

        tutorBidRepository.save(bid);

        notificationService.sendNotification(post.getUser().getId()
                , "Post" + post.getId()
                , "User "+ sender.getEmail() +" just offered to solve your problem."
                , post.getId());

        return tutorBidMapper.toTutorBidResponse(bid);
    }

    @Override
    public List<TutorBidResponse> getAllTutorBid(String postId) {
        return tutorBidRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream().map(tutorBidMapper::toTutorBidResponse).toList();
    }
    @Override
    @PreAuthorize("hasRole('TUTOR')")
    public PageResponse<?> getMyTutorBid(int pageNo, int pageSize, String... sorts){
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
        Page<TutorBid> tutorBids = tutorBidRepository.findAllByIsDeletedFalseAndUserId(userId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(tutorBids.getTotalPages())
                .items(tutorBids.stream().map(tutorBidMapper::toTutorBidResponse).toList())
                .build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public void acceptTutor(String tutorBidId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        var bid =  tutorBidRepository.findById(tutorBidId)
                .orElseThrow(()-> new AppException(ErrorCode.TUTOR_BID_NOT_FOUND));
        var post = bid.getPost();

        if(!Objects.equals(post.getUser().getId(), userId)) throw new AppException(ErrorCode.FORBIDDEN);

        if(!bid.getStatus().equals(BiddingStatus.PENDING)) throw new AppException(ErrorCode.BID_NOT_PENDING);

        var balance = accountBalanceRepository.findByUserId(userId)
                .orElseThrow(()->new AppException(ErrorCode.ACCOUNT_BALANCE_NOT_FOUND));

        if(balance.getBalance() < bid.getProposedPrice()){
            throw new AppException(ErrorCode.ACCOUNT_DONT_HAVE_ENOUGH_MONEY);
        }
        balance.setBalance(balance.getBalance() - bid.getProposedPrice());
        accountBalanceRepository.save(balance);

        if(post.isAccepted()) throw new AppException(ErrorCode.POST_ALREADY_ACCEPTED);
        post.setAccepted(true);
        post.getTutorBids()
                .forEach(tb -> {
                    if(tb.getStatus().equals(BiddingStatus.PENDING))tb.setStatus(BiddingStatus.REJECTED);
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
                .users(users)
                .build();

        var transaction = UserTransaction.builder()
                .sender(sender.getAccountBalance())
                .receiver(receiver.getAccountBalance())
                .amount(bid.getProposedPrice())
                .build();
        post.setChatRoom(room);
        post.setUserTransaction(transaction);
        room.setPost(post);
        transaction.setPost(post);

        post = postRepository.save(post);

        notificationService.sendNotification(bid.getUser().getId()
                , "Post (Id: " + bid.getPost().getId() + ")"
                , "User "+ sender.getEmail() +" has accepted your offer."
                , room.getId());

        //Set job để đẩy thông báo timeout lên FE
        serviceJob.addCheckRoomTimeout(post.getChatRoom().getId(), post.getChatRoom().getCreatedAt());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public void cancelTutorBid(String tutorBidId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        var bid =  tutorBidRepository.findById(tutorBidId)
                .orElseThrow(()-> new AppException(ErrorCode.TUTOR_BID_NOT_FOUND));
        if(!bid.getPost().getUser().getId().equals(userId)) throw new AppException(ErrorCode.FORBIDDEN);
        if(!bid.getStatus().equals(BiddingStatus.PENDING)) throw new AppException(ErrorCode.BID_NOT_PENDING);
        bid.setStatus(BiddingStatus.CANCELED);
        tutorBidRepository.save(bid);

        notificationService.sendNotification(bid.getUser().getId()
                , "Post (Id: " + bid.getPost().getId() + ")"
                , "User "+ bid.getUser().getEmail() +" has cancel your offer."
                , bid.getPost().getId());
    }
}
