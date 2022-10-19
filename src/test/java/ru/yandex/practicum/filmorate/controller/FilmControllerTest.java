package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@Validated
class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = new Film(0,"FilmOne", "New Film", LocalDate.of(1895,12,31), Duration.ofSeconds(12000));
    }

    @Test
    void createFilm() {
        filmController.createFilm(film);
        assertEquals(filmController.getFilms().size(), 1, "Фильм не завелся");
    }

    @Test
    void updateFilm() {
        filmController.createFilm(film);
        assertEquals(filmController.getFilms().size(), 1, "Фильм не завелся");
        assertAll("Значение не изменилось",
                () -> assertEquals(film.getName(), filmController.getFilms().get(1).getName()),
                () -> assertEquals(film.getDescription(), filmController.getFilms().get(1).getDescription()),
                () -> assertEquals(film.getReleaseDate(), filmController.getFilms().get(1).getReleaseDate()),
                () -> assertEquals(film.getDuration(), filmController.getFilms().get(1).getDuration())
        );

        Film filmTwo = new Film(film.getId(),"Film", "Film", LocalDate.of(1899,11,30), Duration.ofSeconds(15000));
        filmController.updateFilm(filmTwo);
        assertEquals(filmController.getFilms().size(), 1, "Фильм не обновился");
        assertAll("Значение не изменилось",
                () -> assertEquals(filmTwo.getName(), filmController.getFilms().get(1).getName()),
                () -> assertEquals(filmTwo.getDescription(), filmController.getFilms().get(1).getDescription()),
                () -> assertEquals(filmTwo.getReleaseDate(), filmController.getFilms().get(1).getReleaseDate()),
                () -> assertEquals(filmTwo.getDuration(), filmController.getFilms().get(1).getDuration())
        );
    }
}