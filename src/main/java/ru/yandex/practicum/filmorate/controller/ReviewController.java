package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

@Slf4j
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto addReview(@Valid @RequestBody ReviewDto reviewDto) {
        log.info("POST /reviews - Запрос на добавление отзыва: {}", reviewDto);
        ReviewDto createReviewDto = reviewService.addReview(reviewDto);
        log.info("POST /reviews - Ответ на добавление отзыва: {}", createReviewDto);
        return createReviewDto;
    }

    @PutMapping
    public ReviewDto updateReview(@Valid @RequestBody ReviewDto reviewDto) {
        log.info("POST /reviews - Запрос на обновление отзыва: {}", reviewDto);
        ReviewDto updatedReviewDto = reviewService.updateReview(reviewDto);
        log.info("POST /reviews - Ответ на обновление отзыва: {}", updatedReviewDto);
        return updatedReviewDto;
    }

    @DeleteMapping("/{reviewId}")
    public ReviewDto deleteReview(@PathVariable int reviewId) {
        log.info("POST /reviews - Запрос на удаление отзыва с id: {}", reviewId);
        ReviewDto reviewDto = reviewService.deleteReview(reviewId);
        log.info("POST /reviews - Ответ на удаление отзыва: {}", reviewDto);
        return reviewDto;
    }

    @GetMapping("/{reviewId}")
    public ReviewDto getReviewById(@PathVariable int reviewId) {
        log.info("POST /reviews - Запрос на получение отзыва с id: {}", reviewId);
        ReviewDto reviewDto = reviewService.getReviewById(reviewId);
        log.info("POST /reviews - Ответ на получение отзыва: {}", reviewDto);
        return reviewDto;
    }

    @GetMapping
    public Collection<ReviewDto> getReviewsByFilmId(@RequestParam(required = false) Integer filmId,
                                                    @RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("POST /reviews - Запрос на получение отзывов по id фильма: {}", filmId);
        Collection<ReviewDto> reviewsDto = reviewService.getReviewsByFilmId(filmId, count);
        log.info("POST /reviews - Ответ на получение отзывов по id фильма: {}", reviewsDto);
        return reviewsDto;
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public ReviewDto addLikeToReview(@PathVariable int reviewId,
                                     @PathVariable int userId) {
        log.info("POST /reviews - Запрос на добавление лайка отзыву: {}, от пользователя: {}", reviewId, userId);
        ReviewDto reviewDto = reviewService.addLikeToReview(reviewId, userId);
        log.info("POST /reviews - Ответ на на добавление лайка отзыву: {}", reviewDto);
        return reviewDto;
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public ReviewDto addDislikeToReview(@PathVariable int reviewId,
                                        @PathVariable int userId) {
        log.info("POST /reviews - Запрос на добавление дизлайка отзыву: {}, от пользователя: {}", reviewId, userId);
        ReviewDto reviewDto = reviewService.addDislikeToReview(reviewId, userId);
        log.info("POST /reviews - Ответ на на добавление дизлайка отзыву: {}", reviewDto);
        return reviewDto;
    }

    @DeleteMapping("{reviewId}/like/{userId}")
    public ReviewDto deleteLikeFromReview(@PathVariable int reviewId,
                                          @PathVariable int userId) {
        log.info("POST /reviews - Запрос на удаление лайка отзыву: {}, от пользователя: {}", reviewId, userId);
        ReviewDto reviewDto = reviewService.deleteLikeFromReview(reviewId, userId);
        log.info("POST /reviews - Ответ на на удаление лайка отзыву: {}", reviewDto);
        return reviewDto;
    }

    @DeleteMapping("{reviewId}/dislike/{userId}")
    public ReviewDto deleteDislikeFromReview(@PathVariable int reviewId,
                                             @PathVariable int userId) {
        log.info("POST /reviews - Запрос на удаление дизлайка отзыву: {}, от пользователя: {}", reviewId, userId);
        ReviewDto reviewDto = reviewService.deleteDislikeFromReview(reviewId, userId);
        log.info("POST /reviews - Ответ на на удаление дизлайка отзыву: {}", reviewDto);
        return reviewDto;
    }
}