package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.SubjectRequest;
import com.grabtutor.grabtutor.dto.response.SubjectResponse;
import com.grabtutor.grabtutor.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    Subject toSubject(SubjectRequest subjectRequest);
    SubjectResponse toSubjectResponse(Subject subject);
    void updateSubjectFromRequest(SubjectRequest subjectRequest,@MappingTarget Subject subject);
}
