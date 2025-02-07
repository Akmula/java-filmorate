package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.FilmLikeRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

@ComponentScan("ru/yandex/practicum/filmorate/*")
class FilmRepositoryTest {
    private final UserRepository userStorage;
    private final FilmRepository filmStorage;
    private final FilmLikeRepository likeStorage;
    Film newFilm;
    User newUser;

    @BeforeEach
    public void setUp() {
        newFilm = Film.builder()
                .name("Test film")
                .description("Test description")
                .duration(100)
                .releaseDate(LocalDate.of(2000, 12, 12))
                .mpa(MPA.builder()
                        .id(1)
                        .build())
                .build();

        newUser = User.builder()
                .login("Test user")
                .email("test@test.com")
                .name("Test user")
                .birthday(LocalDate.of(1980, 12, 12))
                .build();
    }

    @Test
    public void testCreateFilm() {
        filmStorage.createFilm(newFilm);
        final int filmId = newFilm.getId();
        Optional<Film> filmOptional = filmStorage.getFilmById(filmId);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", filmId));
    }

    @Test
    public void testUpdateFilm() {
        filmStorage.createFilm(newFilm);
        newFilm.setName("Updated film");
        filmStorage.updateFilm(newFilm);
        final int filmId = newFilm.getId();
        Optional<Film> filmOptional = filmStorage.getFilmById(filmId);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Updated film"));
    }

    @Test
    public void testGetFilmById() {
        filmStorage.createFilm(newFilm);
        final int filmId = newFilm.getId();
        Optional<Film> filmOptional = filmStorage.getFilmById(filmId);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", filmId));
    }

    @Test
    public void testGetAllFilms() {
        filmStorage.createFilm(newFilm);
        filmStorage.createFilm(newFilm);
        Collection<Film> films = filmStorage.getAllFilms();
        assertThat(films.size()).isEqualTo(2);
    }

    @Test
    public void testAddLikeFilm() {
        filmStorage.createFilm(newFilm);
        userStorage.createUser(newUser);
        final int filmId = newFilm.getId();
        final int userId = newUser.getId();
        filmStorage.addLikeFilm(filmId, userId);
        assertEquals(1, likeStorage.getLikeFilm(filmId).size());
    }

    @Test
    public void testDeleteLikeFilm() {
        filmStorage.createFilm(newFilm);
        userStorage.createUser(newUser);
        final int filmId = newFilm.getId();
        final int userId = newUser.getId();
        filmStorage.addLikeFilm(filmId, userId);
        filmStorage.deleteLikeFilm(filmId, userId);
        assertEquals(0, likeStorage.getLikeFilm(filmId).size());
    }

    @Test
    public void testGetPopularFilms() {
        filmStorage.createFilm(newFilm);
        filmStorage.createFilm(newFilm);
        userStorage.createUser(newUser);
        final int filmId = newFilm.getId();
        final int userId = newUser.getId();
        filmStorage.addLikeFilm(filmId, userId);
        Collection<Film> popular = filmStorage.getPopularFilms(3, null, null);
        Film popularFilm = popular.stream().findFirst().orElse(null);
        assert popularFilm != null;
        assertEquals(filmId, popularFilm.getId());
    }
}