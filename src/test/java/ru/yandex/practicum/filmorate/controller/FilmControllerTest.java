package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private final MpaDbStorage mpaDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final UserDbStorage userDbStorage;
    private final Film film = new Film(1, "Film",
            "Description", LocalDate.of(1985, 12, 12),
            144, 3, new Mpa(1, null));

    @Test
    void addTestFilm() {
        filmDbStorage.create(film);
        assertNotNull(filmDbStorage.findById(film.getId()));
        filmDbStorage.remove(film);
    }

    @Test
    void updateFilm() {
        filmDbStorage.create(film);
        film.setDescription("New Film");
        filmDbStorage.update(film);
        assertEquals("New Film", filmDbStorage.findById(film.getId()).getDescription());
        filmDbStorage.remove(film);
    }

    @Test
    void testFilmAll() {
        Film film2 = new Film(2, "Film2",
                "Description2", LocalDate.of(1999, 11, 11),
                111, 2, new Mpa(2, null));
        filmDbStorage.create(film2);
        filmDbStorage.create(film);
        assertEquals(2, filmDbStorage.getAllFilms().size());
        filmDbStorage.remove(film2);
        assertEquals(1, filmDbStorage.getAllFilms().size());
    }

    @Test
    void getGenreTest() {
        assertEquals("Комедия", genreDbStorage.getId(1).getName());
        assertEquals(6, genreDbStorage.getAll().size());
    }

    @Test
    void getMpaTest() {
        assertEquals("G", mpaDbStorage.getId(1).getName());
        assertEquals(5, mpaDbStorage.getAll().size());
    }

    @Test
    void likeFilm() {
        filmDbStorage.create(film);
        User user = new User(1, "Login2", "Name2", "email2@mail.ru",
                LocalDate.of(1978, 11,11));
        userDbStorage.create(user);
        filmDbStorage.addLike(film.getId(), user.getId());
        assertEquals(1, filmDbStorage.getFilmLikeUser(film.getId()).size());

        filmDbStorage.removeLike(film.getId(), user.getId());
        assertEquals(0, filmDbStorage.getFilmLikeUser(film.getId()).size());
        userDbStorage.remove(user);
    }

    @Test
    void getTestPopularFilm() {
        filmDbStorage.create(film);
        User user = new User(1, "Login", "Name", "email@mail.ru",
                LocalDate.of(1978, 11,11));
        User user2 = new User(2, "Login2", "Name2", "email2@mail.ru",
                LocalDate.of(1978, 12,12));
        userDbStorage.create(user);
        Film film2 = new Film(2, "Film2",
                "Description2", LocalDate.of(1999, 11, 11),
                111, 2, new Mpa(2, null));
        userDbStorage.create(user2);
        filmDbStorage.create(film2);
        filmDbStorage.addLike(film.getId(), user.getId());
        filmDbStorage.addLike(film.getId(), user2.getId());
        filmDbStorage.addLike(film2.getId(), user.getId());
        filmDbStorage.addLike(film2.getId(), user2.getId());
        assertEquals(2, filmDbStorage.getFilmPopular(2).size());
        assertEquals(1, filmDbStorage.getFilmPopular(1).size());

        filmDbStorage.removeLike(film2.getId(), user.getId());
        assertEquals(1, filmDbStorage.getFilmPopular(1).size());
    }
}
