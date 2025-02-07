package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.ReviewLikeRepository;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;

@Slf4j
@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final ReviewLikeRepository reviewLikeRepository;
    private final FilmService filmService;
    private final UserService userService;

    public ReviewService(@Qualifier("reviewRepository") ReviewStorage reviewStorage,
                         ReviewLikeRepository reviewLikeRepository,
                         FilmService filmService,
                         UserService userService) {
        this.reviewStorage = reviewStorage;
        this.reviewLikeRepository = reviewLikeRepository;
        this.filmService = filmService;
        this.userService = userService;
    }

    public ReviewDto addReview(ReviewDto reviewDto) {
        log.info("ReviewService - Добавление отзыва: {}", reviewDto);
        filmService.validate(reviewDto.getFilmId(), reviewDto.getUserId());
        reviewDto.setUseful(0);
        Review review = ReviewMapper.mapToReview(reviewDto);
        ReviewDto createReviewDto = ReviewMapper.mapToReviewDto(reviewStorage.addReview(review));
        log.info("ReviewService - Добавлен отзыв - {}", createReviewDto);
        return createReviewDto;
    }

    public ReviewDto updateReview(ReviewDto reviewDto) {
        log.info("ReviewService - обновление отзыва: {}", reviewDto);
        filmService.validate(reviewDto.getFilmId(), reviewDto.getUserId());
        Review review = ReviewMapper.mapToReview(reviewDto);
        ReviewDto oldReviewDto = getReviewById(reviewDto.getReviewId());
        review.setUseful(oldReviewDto.getUseful());
        ReviewDto updatedReviewDto = ReviewMapper.mapToReviewDto(reviewStorage.updateReview(review));
        log.info("ReviewService - Обновлен отзыв - {}", updatedReviewDto);
        return updatedReviewDto;
    }

    public ReviewDto deleteReview(Integer reviewId) {
        log.info("ReviewService - удаление отзыва с id: {}", reviewId);
        Review deletedReview = reviewStorage.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв с id: " + reviewId + " не найден!"));
        reviewStorage.deleteReview(reviewId);
        ReviewDto reviewDto = ReviewMapper.mapToReviewDto(deletedReview);
        log.info("ReviewService - Удален отзыв - {}", reviewDto);
        return reviewDto;
    }

    public ReviewDto getReviewById(Integer reviewId) {
        log.info("ReviewService - получение отзыва по id: {}", reviewId);
        Review review = reviewStorage.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв с id: " + reviewId + " не найден!"));
        ReviewDto reviewDto = ReviewMapper.mapToReviewDto(review);
        log.info("ReviewService - получен отзыв - {}", reviewDto);
        return reviewDto;
    }

    public Collection<ReviewDto> getReviewsByFilmId(Integer filmId, Integer count) {
        log.info("ReviewService - получение отзывов по id фильма: {}", filmId);
        Collection<Review> reviews = reviewStorage.getReviewsByFilmId(filmId, count);
        log.info("ReviewService - получен отзывы  по id фильма- {}", reviews);
        return ReviewMapper.mapToReviewDtoList(reviews);
    }

    public ReviewDto addLikeToReview(Integer reviewId, Integer userId) {
        log.info("ReviewService - добавление лайка отзыву с id - {}, от пользователя: {}.", reviewId, userId);
        getReviewById(reviewId);
        userService.getUserById(userId);
        reviewLikeRepository.addLikeToReview(reviewId, userId);
        log.info("ReviewService - Добавлен лайк отзыву с id - {}, пользователем: {}.", reviewId, userId);
        return getReviewById(reviewId);
    }

    public ReviewDto addDislikeToReview(Integer reviewId, Integer userId) {
        log.info("ReviewService - добавление дизлайка отзыву с id - {}, от пользователя: {}.", reviewId, userId);
        getReviewById(reviewId);
        userService.getUserById(userId);
        reviewLikeRepository.addDislikeToReview(reviewId, userId);
        log.info("ReviewService - Добавлен дизлайк отзыву с id - {}, пользователем: {}.", reviewId, userId);
        return getReviewById(reviewId);
    }

    public ReviewDto deleteLikeFromReview(Integer reviewId, Integer userId) {
        log.info("ReviewService - удаление лайка у отзыва с id - {}, пользователем: {}.", reviewId, userId);
        getReviewById(reviewId);
        userService.getUserById(userId);
        reviewLikeRepository.deleteLikeFromReview(reviewId, userId);
        log.info("ReviewService - удален лайк у отзыва с id - {}, пользователем: {}.", reviewId, userId);
        return getReviewById(reviewId);
    }

    public ReviewDto deleteDislikeFromReview(Integer reviewId, Integer userId) {
        log.info("ReviewService - удаление дизлайка у отзыва с id - {}, пользователем: {}.", reviewId, userId);
        getReviewById(reviewId);
        userService.getUserById(userId);
        reviewLikeRepository.deleteDislikeFromReview(reviewId, userId);
        log.info("ReviewService - удален дизлайк у отзыва с id - {}, пользователем: {}.", reviewId, userId);
        return getReviewById(reviewId);
    }
}