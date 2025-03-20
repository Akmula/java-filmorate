package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmLikeRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecommendationService {

    private static FilmLikeRepository filmLikeRepository;
    private static FilmRepository filmRepository;

    public RecommendationService(FilmLikeRepository filmLikeRepository, FilmRepository filmRepository) {
        RecommendationService.filmLikeRepository = filmLikeRepository;
        RecommendationService.filmRepository = filmRepository;
    }

    public Collection<Film> getRecommendations(Integer userId) {
        List<Integer> userFavoriteMoviesId = filmLikeRepository.getFilmIdsByUserId(userId);
        List<Integer> otherUserFavoriteMoviesId = new ArrayList<>();
        List<Integer> otherUserIds;

        for (Integer filmId : userFavoriteMoviesId) {

            otherUserIds = filmLikeRepository.getUserIdsByFilmId(filmId);

            for (Integer id : otherUserIds) {
                if (!id.equals(userId)) {
                    if (otherUserFavoriteMoviesId.isEmpty()) {
                        otherUserFavoriteMoviesId = filmLikeRepository.getFilmIdsByUserId(id);
                    } else {
                        List<Integer> filmIds = filmLikeRepository.getFilmIdsByUserId(id);
                        if (otherUserFavoriteMoviesId.size() < filmIds.size()) {
                            otherUserFavoriteMoviesId = filmIds;
                        }
                    }
                }
            }
        }

        otherUserFavoriteMoviesId.removeAll(userFavoriteMoviesId);

        return otherUserFavoriteMoviesId.stream()
                .map(filmId -> filmRepository.getFilmById(filmId)
                        .orElseThrow(() -> new NotFoundException("Фильм c id " + filmId + " не найден.")))
                .collect(Collectors.toSet());
    }
}