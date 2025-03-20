package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

@Service
public final class ReviewMapper {
    public static Review mapToReview(ReviewDto reviewDto) {
        return Review.builder()
                .reviewId(reviewDto.getReviewId())
                .content(reviewDto.getContent())
                .isPositive(reviewDto.getIsPositive())
                .userId(reviewDto.getUserId())
                .filmId(reviewDto.getFilmId())
                .useful(reviewDto.getUseful())
                .build();
    }

    public static ReviewDto mapToReviewDto(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .isPositive(review.getIsPositive())
                .userId(review.getUserId())
                .filmId(review.getFilmId())
                .useful(review.getUseful())
                .build();
    }

    public static Collection<ReviewDto> mapToReviewDtoList(Collection<Review> reviews) {
        return reviews.stream()
                .map(ReviewMapper::mapToReviewDto)
                .toList();
    }
}