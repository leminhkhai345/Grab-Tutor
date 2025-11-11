package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.response.PostStatusStatisticsResponse;
import com.grabtutor.grabtutor.dto.response.ReviewStarStatisticResponse;
import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.enums.PostStatus;
import com.grabtutor.grabtutor.repository.PostRepository;
import com.grabtutor.grabtutor.repository.ReviewRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticServiceImpl implements StatisticService {
    UserRepository userRepository;
    PostRepository postRepository;
    ReviewRepository reviewRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PostStatusStatisticsResponse getPostStatusStatistics(){
        var posts = postRepository.findAll();
        int open = 0;
        int closed = 0;
        int solved = 0;
        int reported = 0;
        for(Post post : posts){
            switch(post.getStatus()){
                case OPEN -> open++;
                case CLOSED -> closed++;
                case SOLVED -> solved++;
                case REPORTED -> reported++;
            }
        }
        return PostStatusStatisticsResponse.builder()
                .OPEN(open)
                .CLOSED(closed)
                .SOLVED(solved)
                .REPORTED(reported)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ReviewStarStatisticResponse getReviewStarStatistics(){
        var reviews = reviewRepository.findAll();
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        for(var review : reviews){
            switch(review.getStars()){
                case 1 -> one++;
                case 2 -> two++;
                case 3 -> three++;
                case 4 -> four++;
                case 5 -> five++;
            }
        }
        return ReviewStarStatisticResponse.builder()
                .one(one)
                .two(two)
                .three(three)
                .four(four)
                .five(five)
                .build();
    }


}
