package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Review addReview(Review review);

    Review updateReview(Review review);

    Review deleteReview(Integer reviewId);

    Optional<Review> getReviewById(Integer reviewId);

    Collection<Review> getReviewsByFilmId(Integer filmId, Integer count);

}