package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.CourseRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.VirtualTransactionResponse;
import com.grabtutor.grabtutor.dto.response.CourseResponse;
import com.grabtutor.grabtutor.entity.Course;
import com.grabtutor.grabtutor.entity.Subject;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.entity.VirtualTransaction;
import com.grabtutor.grabtutor.enums.TransactionStatus;
import com.grabtutor.grabtutor.enums.TransactionType;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.CourseMapper;
import com.grabtutor.grabtutor.mapper.VirtualTransactionMapper;
import com.grabtutor.grabtutor.repository.CourseRepository;
import com.grabtutor.grabtutor.repository.SubjectRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.repository.VirtualTransactionRepository;
import com.grabtutor.grabtutor.service.CourseService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;
    UserRepository userRepository;
    SubjectRepository subjectRepository;
    VirtualTransactionRepository virtualTransactionRepository;
    VirtualTransactionMapper virtualTransactionMapper;

    @PreAuthorize("hasRole('TUTOR')")
    @Override
    public CourseResponse createCourse(CourseRequest request, Set<String> subjectIds) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course = courseMapper.toCourse(request);
        if(subjectIds != null && !subjectIds.isEmpty()) {
            Set<Subject> subjects = new HashSet<>(subjectRepository.findAllById(subjectIds));
            course.setSubjects(subjects);
        }
        course.setTutor(tutor);
        return courseMapper.toCourseResponse(courseRepository.save(course));
    }

    @PreAuthorize("hasRole('TUTOR')")
    @Override
    public CourseResponse updateCourse(String courseId, CourseRequest request,
                                       Set<String> subjectIds) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
        if(course.isDeleted()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if(!course.getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        courseMapper.updateCourseFromRequest(request, course);
        if(!subjectIds.isEmpty()){
            Set<Subject> subjects = new HashSet<>(subjectRepository.findAllById(subjectIds));
            course.setSubjects(subjects);
        }
        return courseMapper.toCourseResponse(courseRepository.save(course));
    }


    @Override
    public CourseResponse getCourseByCourseId(String courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(course.isDeleted()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if(!course.getTutor().getId().equals(userId) && !course.isPublished()) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        CourseResponse response = courseMapper.toCourseResponse(course);
        boolean isEnrolled = course.getEnrolledUsers().stream()
                .anyMatch(user -> user.getId().equals(userId));
        response.setEnrolled(isEnrolled);
        return response;
    }

    @Override
    public void deleteCourse(String courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String tutorId = jwt.getClaimAsString("userId");
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(course.isDeleted()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        if(!course.getTutor().getId().equals(tutorId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        course.setDeleted(true);
        courseRepository.save(course);
    }

    @Override
    public List<CourseResponse> getAllCoursesByTutorId(String tutorId, int pageNo, int pageSize, String... sorts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));
        Page<Course> courses = courseRepository.findByTutorIdAndIsDeletedFalse(tutorId, pageable);
        return courses.stream()
                .map(courseMapper::toCourseResponse)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public VirtualTransactionResponse enrollCourse(String courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        user.getEnrolledCourses().stream()
                .filter(c -> c.getId().equals(courseId))
                .findFirst()
                .ifPresent(c -> {
                    throw new AppException(ErrorCode.USER_ALREADY_ENROLLED_COURSE);
                });

        var accountBalance = user.getAccountBalance();
        if(accountBalance.getBalance() < course.getPrice()) {
            throw new AppException(ErrorCode.ACCOUNT_DONT_HAVE_ENOUGH_MONEY);
        }

        VirtualTransaction enrollment = virtualTransactionMapper.toVirtualTransaction(courseId);
        enrollment.setUser(user);
        enrollment.setAmount(course.getPrice());
        accountBalance.setBalance(accountBalance.getBalance() - course.getPrice());
        enrollment.setCompletedAt(LocalDateTime.now());
        enrollment.setStatus(TransactionStatus.SUCCESS);
        enrollment.setType(TransactionType.ENROLLMENT);

        virtualTransactionRepository.save(enrollment);
        user.getEnrolledCourses().add(course);
        userRepository.save(user);
        course.getEnrolledUsers().add(user);
        courseRepository.save(course);

        return VirtualTransactionResponse.builder()
                .userId(userId)
                .amount(course.getPrice())
                .status(enrollment.getStatus())
                .type(enrollment.getType())
                .transactionDate(enrollment.getTransactionDate())
                .completedAt(enrollment.getCompletedAt())
                .build();

    }

    @Override
    public PageResponse<?> getMyEnrolledCourses(int pageNo, int pageSize, String... sorts) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");

        List<Sort.Order> orders = new ArrayList<>();

        if (sorts != null && sorts.length > 0) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)", Pattern.CASE_INSENSITIVE);

            for (String sortBy : sorts) {
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.matches()) {
                    String direction = matcher.group(3);
                    String field = matcher.group(1);

                    if (direction.equalsIgnoreCase("desc")) {
                        orders.add(Sort.Order.desc(field));
                    } else {
                        orders.add(Sort.Order.asc(field));
                    }
                }
            }
        }

        Pageable pageable = orders.isEmpty()
                ? PageRequest.of(pageNo, pageSize)
                : PageRequest.of(pageNo, pageSize, Sort.by(orders));

        Page<Course> courses = courseRepository.findByEnrolledUsersIdAndIsDeletedFalse(userId, pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(courses.getTotalPages())
                .items(courses.stream().map(courseMapper::toCourseResponse).toList())
                .build();
    }
}
