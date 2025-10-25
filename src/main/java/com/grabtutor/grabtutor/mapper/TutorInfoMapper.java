package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.TutorInfoRequest;
import com.grabtutor.grabtutor.dto.request.TutorRequest;
import com.grabtutor.grabtutor.dto.request.UserRequest;
import com.grabtutor.grabtutor.entity.TutorInfo;
import com.grabtutor.grabtutor.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TutorInfoMapper {
    TutorInfo toTutorInfo(TutorInfoRequest request);
    TutorInfo toTutorInfo(TutorRequest tutorRequest);
    void updateTutorInfoFromRequest(TutorInfoRequest tutorInfoRequest, @MappingTarget TutorInfo tutorInfo);

}
