package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.TutorInfoRequest;
import com.grabtutor.grabtutor.entity.TutorInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TutorInfoMapper {
    TutorInfo toTutorInfo(TutorInfoRequest request);
}
