package com.grabtutor.grabtutor.mapper;

import com.grabtutor.grabtutor.dto.request.TutorInfoRequest;
import com.grabtutor.grabtutor.dto.request.TutorRequest;
import com.grabtutor.grabtutor.dto.request.UserRequest;
import com.grabtutor.grabtutor.dto.response.TutorInfoResponse;
import com.grabtutor.grabtutor.entity.TutorInfo;
import com.grabtutor.grabtutor.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TutorInfoMapper {
    TutorInfo toTutorInfo(TutorInfoRequest request);
    TutorInfo toTutorInfo(TutorRequest tutorRequest);
    default TutorInfoResponse toTutorInfoResponse(TutorInfo tutorInfo) {
        if ( tutorInfo == null ) {
            return null;
        }

        TutorInfoResponse.TutorInfoResponseBuilder tutorInfoResponse = TutorInfoResponse.builder();

        tutorInfoResponse.userId( tutorInfo.getUser() != null ? tutorInfo.getUser().getId() : null );
        tutorInfoResponse.nationalId( tutorInfo.getNationalId() );
        tutorInfoResponse.university( tutorInfo.getUniversity() );
        tutorInfoResponse.highestAcademicDegree( tutorInfo.getHighestAcademicDegree() );
        tutorInfoResponse.major( tutorInfo.getMajor() );
        tutorInfoResponse.averageStars( tutorInfo.getAverageStars() );
        tutorInfoResponse.problemSolved( tutorInfo.getProblemSolved() );

        return tutorInfoResponse.build();
    }
    void updateTutorInfoFromRequest(TutorInfoRequest tutorInfoRequest, @MappingTarget TutorInfo tutorInfo);

}
