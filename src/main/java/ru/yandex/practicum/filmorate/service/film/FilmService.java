package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MPARepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FilmService {

    private static final int MAX_SIZE_DESCRIPTION = 200;
    private static final LocalDate INTERNATIONAL_FILM_DAY = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MPARepository mpaRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;


    public FilmService(@Qualifier("filmRepository") FilmStorage filmStorage,
                       @Qualifier("userRepository") UserStorage userStorage,
                       MPARepository mpaRepository,
                       GenreRepository genreRepository,
                       DirectorRepository directorRepository
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreRepository = genreRepository;
        this.mpaRepository = mpaRepository;
        this.directorRepository = directorRepository;
    }

    public FilmDto createFilm(FilmRequest request) {
        log.info("FilmService - Создание фильма - {}", request);
        FilmRequest updatedRequest = updateFieldsForRequest(request);
        Film film = FilmMapper.mapToFilm(updatedRequest);
        FilmDto filmDto = FilmMapper.mapToFilmDto(filmStorage.createFilm(film));
        log.info("FilmService - Добавлен фильм - {}", filmDto);
        return filmDto;
    }

    public FilmDto updateFilm(FilmRequest request) {
        log.info("FilmService - Обновление фильма");
        FilmRequest updatedRequest = updateFieldsForRequest(request);
        Film updatedFilm = filmStorage.getFilmById(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, updatedRequest))
                .orElseThrow(() -> new FilmNotFoundException(updatedRequest.getId()));

        FilmDto filmDto = FilmMapper.mapToFilmDto(filmStorage.updateFilm(updatedFilm));
        log.info("FilmService - Обновленный фильм - {}", filmDto);
        return filmDto;
    }

    public FilmDto deleteFilm(Integer filmId) {
        log.info("FilmService - Удаление фильма");
        Film deletedFilm = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        filmStorage.deleteFilm(deletedFilm);
        FilmDto filmDto = FilmMapper.mapToFilmDto(deletedFilm);
        log.info("FilmService - Удаленный фильм: {}", filmDto);
        return filmDto;
    }

    public Collection<FilmDto> getAllFilms() {
        log.info("FilmService - Получение всех фильмов");
        Collection<FilmDto> filmsDto = filmStorage.getAllFilms()
                .stream().map(FilmMapper::mapToFilmDto).toList();

        log.info("FilmService - Список всех фильмов получен");
        return filmsDto;
    }

    public FilmDto getFilmById(Integer filmId) {
        log.info("FilmService - Получения фильма по id - {}", filmId);
        FilmDto filmDto = filmStorage.getFilmById(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new FilmNotFoundException(filmId));

        log.info("FilmService - Получен фильм по id - {}", filmDto);
        return filmDto;
    }

    public FilmDto addLikeFilm(Integer filmId, Integer userId) {
        log.info("FilmService - Добавление лайка фильм с id - {}, от пользователя с id - {}", filmId, userId);
        validate(filmId, userId);
        filmStorage.addLikeFilm(filmId, userId);
        log.info("FilmService - Пользователь с id - {} поставил лайк фильму с id - {}", userId, filmId);
        return getFilmById(filmId);
    }

    public FilmDto deleteLikeFilm(Integer filmId, Integer userId) {
        log.info("FilmService - Удаление лайка из фильма с id - {}, пользователем с id - {}", filmId, userId);
        validate(filmId, userId);
        filmStorage.deleteLikeFilm(filmId, userId);
        log.info("FilmService - Пользователь с id - {} удалил лайк у фильма с id - {}", userId, filmId);
        return getFilmById(filmId);
    }

    public Collection<FilmDto> getPopularFilms(Integer count, Integer genreId, Integer year) {
        log.info("FilmService - получение популярных фильмов по параметрам {} и {}. Выводить {} фильмов",
                count, genreId, year);
        List<FilmDto> popularMovies = filmStorage.getPopularFilms(count, genreId, year)
                .stream().map(FilmMapper::mapToFilmDto).toList();

        log.info("FilmService - Получено {} популярных фильмов", count);
        return popularMovies;
    }

    public Collection<FilmDto> getCommonFilms(Integer userId, Integer friendId) {
        log.info("FilmService - получение общих фильмов пользователей {} и {}.", userId, friendId);
        return filmStorage.getCommonFilms(userId, friendId)
                .stream().map(FilmMapper::mapToFilmDto).toList();
    }

    public Collection<FilmDto> getDirectorFilms(Integer directorId, String sortBy) {
        log.info("FilmService - получение фильмов режиссера с id {}.", directorId);
        return filmStorage.getDirectorFilms(directorId, sortBy)
                .stream().map(FilmMapper::mapToFilmDto).toList();
    }

    public Collection<FilmDto> search(String query, String by) {
        log.info("FilmService - поиск фиьмов по запросу {}.", query);
        return filmStorage.search(query, by)
                .stream().map(FilmMapper::mapToFilmDto).toList();
    }

    public void validate(Integer filmId, Integer userId) {
        log.info("FilmService - проверка в базе фильма с id - {} и пользователя с id - {}", filmId, userId);
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isEmpty()) {
            throw new FilmNotFoundException(filmId);
        }

        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
    }

    private FilmRequest updateFieldsForRequest(FilmRequest filmRequest) {
        log.info("FilmService - установка MPA, жанров и режиссеров");
        validateRequest(filmRequest);
        if (filmRequest.getMpa() != null) {
            int mpaId = filmRequest.getMpa().getId();
            filmRequest.setMpa(mpaRepository.getMpaById(mpaId)
                    .orElseThrow(() -> new NotFoundException("Категория с id - " + mpaId + " не найдена")));
        } else {
            filmRequest.setMpa(MPA.builder().build());
        }

        if (filmRequest.getGenres() != null) {
            Set<Genre> genres = new LinkedHashSet<>();
            for (Genre genre : filmRequest.getGenres()) {
                int genreId = genre.getId();
                genres.add(genreRepository.getGenreById(genreId)
                        .orElseThrow(() -> new NotFoundException("Жанр с id - " + genreId + " не найден")));
            }
            filmRequest.setGenres(genres);
        } else {
            filmRequest.setGenres(new HashSet<>());
        }

        if (filmRequest.getDirectors() != null) {
            Set<Director> directors = new LinkedHashSet<>();
            for (Director director : filmRequest.getDirectors()) {
                int directorId = director.getId();
                directors.add(directorRepository.getDirectorById(directorId)
                        .orElseThrow(() -> new NotFoundException("Режиссер с id - " + directorId + " не найден")));
            }
            filmRequest.setDirectors(directors);
        } else {
            filmRequest.setDirectors(new HashSet<>());
        }

        return filmRequest;
    }

    private static void validateRequest(FilmRequest filmRequest) {
        log.info("FilmService - Валидация фильма: {}", filmRequest);
        if (filmRequest.getName() == null || filmRequest.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым!");
        }
        if (filmRequest.getDescription().length() > MAX_SIZE_DESCRIPTION) {
            throw new ValidationException("Максимальная длина описания — " + MAX_SIZE_DESCRIPTION + " символов!");
        }
        if (filmRequest.getReleaseDate().isBefore(INTERNATIONAL_FILM_DAY)) {
            throw new ValidationException("Дата релиза должна быть не раньше " + INTERNATIONAL_FILM_DAY + "!");
        }
        if (filmRequest.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом!");
        }
    }
}