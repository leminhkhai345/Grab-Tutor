package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.SubjectRequest;
import com.grabtutor.grabtutor.dto.response.PageResponse;
import com.grabtutor.grabtutor.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {
    SubjectResponse createSubject(SubjectRequest subjectRequest);
    SubjectResponse updateSubject(String id, SubjectRequest subjectRequest);
    SubjectResponse getSubjectById(String id);
    SubjectResponse getSubjectByName(String name);
    PageResponse<?> getAllSubjects(int pageNo, int pageSize, String... sorts);
    void deleteSubject(String id);
}
