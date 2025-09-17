package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.SubjectRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.SubjectResponse;
import com.grabtutor.grabtutor.entity.Subject;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.SubjectMapper;
import com.grabtutor.grabtutor.repository.SubjectRepository;
import com.grabtutor.grabtutor.service.SubjectService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    SubjectMapper subjectMapper;

    @Override
    public SubjectResponse createSubject(SubjectRequest subjectRequest) {
        if(subjectRepository.existsByName(subjectRequest.getName())){
            throw new AppException(ErrorCode.SUBJECT_ALREADY_EXISTS);
        }
        Subject subject = subjectMapper.toSubject(subjectRequest);
        return subjectMapper.toSubjectResponse(subjectRepository.save(subject));
    }

    @Override
    public SubjectResponse updateSubject(String id, SubjectRequest subjectRequest) {
        var subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        if(subjectRepository.existsByName(subjectRequest.getName())){
            throw new AppException(ErrorCode.SUBJECT_ALREADY_EXISTS);
        }
        subjectMapper.updateSubjectFromRequest(subjectRequest, subject);
        return subjectMapper.toSubjectResponse(subjectRepository.save(subject));
    }

    @Override
    public SubjectResponse getSubjectById(String id) {
        var subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        return subjectMapper.toSubjectResponse(subject);
    }

    @Override
    public SubjectResponse getSubjectByName(String name) {
        try {
            Subject subject = subjectRepository.findByName(name);
            return subjectMapper.toSubjectResponse(subject);
        } catch (Exception e) {
            throw new AppException(ErrorCode.SUBJECT_NOT_FOUND);
        }

    }

    @Override
    public PageResponse<?> getAllSubjects(int pageNo, int pageSize, String... sorts) {
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
        Page<Subject> subjects = subjectRepository.findAll(pageable);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(subjects.getTotalPages())
                .items(subjects.stream().map(subjectMapper::toSubjectResponse).toList())
                .build();
    }

    @Override
    public void deleteSubject(String id) {
        subjectRepository.deleteById(id);
    }
}
