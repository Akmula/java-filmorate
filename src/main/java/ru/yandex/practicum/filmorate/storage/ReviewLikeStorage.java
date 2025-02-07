package ru.yandex.practicum.filmorate.storage;

public interface ReviewLikeStorage {

    void addLikeToReview(Integer reviewId, Integer userId);

    void addDislikeToReview(Integer reviewId, Integer userId);

    void deleteLikeFromReview(Integer reviewId, Integer userId);

    void deleteDislikeFromReview(Integer reviewId, Integer userId);
}