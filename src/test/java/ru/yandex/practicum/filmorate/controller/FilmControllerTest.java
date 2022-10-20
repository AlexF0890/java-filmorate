package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@Validated
class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = new Film(0,"FilmOne", "New Film", LocalDate.of(1895,12,31), 120);
    }

    @Test
    void createFilm() {
        filmController.createFilm(film);
        assertEquals(filmController.getFilms().size(), 1, "Фильм не завелся");

        Film filmTwo = filmController.getFilms().get(1);
        assertAll("Значение не совпадают",
                () -> assertEquals(film.getName(), filmTwo.getName()),
                () -> assertEquals(film.getDescription(), filmTwo.getDescription()),
                () -> assertEquals(film.getReleaseDate(), filmTwo.getReleaseDate()),
                () -> assertEquals(film.getDuration(), filmTwo.getDuration())
        );
    }

    @SneakyThrows
    @Test
    void updateFilm() {
        filmController.createFilm(film);
        assertEquals(filmController.getFilms().size(), 1, "Фильм не завелся");

        Film filmTwo = new Film(film.getId(),"Film", "Film", LocalDate.of(1899,11,30), 150);
        filmController.updateFilm(filmTwo);
        assertEquals(filmController.getFilms().size(), 1, "Фильм не обновился");
        Film filmThree = filmController.getFilms().get(1);
        assertAll("Значение не изменилось",
                () -> assertEquals(filmTwo.getName(), filmThree.getName()),
                () -> assertEquals(filmTwo.getDescription(), filmThree.getDescription()),
                () -> assertEquals(filmTwo.getReleaseDate(), filmThree.getReleaseDate()),
                () -> assertEquals(filmTwo.getDuration(), filmThree.getDuration())
        );
    }
}