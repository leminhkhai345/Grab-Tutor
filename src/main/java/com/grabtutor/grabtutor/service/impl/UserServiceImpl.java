package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.*;
import com.grabtutor.grabtutor.dto.response.*;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.entity.VerificationRequest;
import com.grabtutor.grabtutor.enums.RequestStatus;
import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.TutorInfoMapper;
import com.grabtutor.grabtutor.mapper.UserMapper;
import com.grabtutor.grabtutor.mapper.VerificationRequestMapper;
import com.grabtutor.grabtutor.repository.TutorInfoRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.repository.VerificationRequestRepository;
import com.grabtutor.grabtutor.service.UserService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    TutorInfoRepository  tutorInfoRepository;
    VerificationRequestRepository  verificationRequestRepository;
    UserMapper userMapper;
    TutorInfoMapper  tutorInfoMapper;
    VerificationRequestMapper verificationRequestMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse addUser(UserRequest userRequest){
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(userRequest);
        Set<Role> roles = new HashSet<>();
        if(userRequest.getRole() == Role.TUTOR.name()){
            roles.add(Role.TUTOR);
        } else if(userRequest.getRole() == Role.ADMIN.name()){
            roles.add(Role.ADMIN);
        } else {
            roles.add(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PostAuthorize("returnObject.email == authentication.name or hasRole('ADMIN')")
    @Override
    public UserResponse getUserById(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.email == authentication.name")
    @Override
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.email == authentication.name or hasRole('ADMIN')")
    @Override
    public UserResponse updateUser(String id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!user.getEmail().equals(userRequest.getEmail()) && userRepository.existsByEmail(userRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        userMapper.updateUserFromRequest(userRequest, user);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(String id){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserResponse changeActive(String id, boolean active){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setActive(active);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PageResponse<?> getAllUsers(int pageNo, int pageSize, String ... sorts){

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
        Page<User> users = userRepository.findAll(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .items(users.stream().map(userMapper::toUserResponse).toList())
                .build();
    }
    @PreAuthorize("hasRole('TUTOR')")
    @Override
    public TutorInfoResponse addInfo(TutorInfoRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(user.getTutorInfo() != null) {
            var existInfo = user.getTutorInfo();
            tutorInfoMapper.updateTutorInfoFromRequest(request,existInfo);
            tutorInfoRepository.save(existInfo);
        }
        else{
            var info = tutorInfoMapper.toTutorInfo(request);
            if(tutorInfoRepository.existsByNationalId(info.getNationalId()))
                throw new AppException(ErrorCode.NATIONAL_ID_ALREADY_EXISTS);
            tutorInfoRepository.save(info);
        }

        return TutorInfoResponse.builder()
                .userId(request.getUserId())
                .nationalId(request.getNationalId())
                .university(request.getUniversity())
                .highestAcademicDegree(request.getHighestAcademicDegree())
                .major(request.getMajor())
                .build();
    }
    @PreAuthorize("hasRole('TUTOR')")
    @Override
    public AccountVerificationResponse submitRequest(AccountVerificationRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(user.getRole() ==  Role.TUTOR && user.isActive()) throw new AppException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
        var newRequest = VerificationRequest.builder()
                .user(user)
                .build();
        verificationRequestRepository.save(newRequest);

        return AccountVerificationResponse.builder()
                .userId(user.getId())
                .requestId(newRequest.getId())
                .status(newRequest.getStatus().toString())
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ApproveResponse approveRequest(ApproveRequest request){
        var req = verificationRequestRepository.getById(request.getRequestId());
        req.setStatus(RequestStatus.APPROVED);
        verificationRequestRepository.save(req);

        return ApproveResponse.builder()
                .requestId(req.getId())
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RejectResponse rejectRequest(RejectRequest request){
        var req = verificationRequestRepository.getById(request.getRequestId());
        req.setStatus(RequestStatus.REJECTED);
        verificationRequestRepository.save(req);

        return RejectResponse.builder()
                .requestId(req.getId())
                .build();
    }

    @Override
    public PageResponse<?> getRequests(int pageNo, int pageSize, String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        for(String sortBy : sorts){
            // firstname:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:|#)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if(matcher.group(2).equalsIgnoreCase(":")){
                    if(matcher.group(3).equalsIgnoreCase("desc")){
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    }
                }
            }
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));
        Page<VerificationRequest> requests = verificationRequestRepository.findAll(pageable);
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(requests.getTotalPages())
                .items(requests.stream().map(verificationRequestMapper::toAccountVerificationResponse))
                .build();
    }
}
