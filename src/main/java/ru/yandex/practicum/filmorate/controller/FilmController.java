package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Validated
@Slf4j
@RequestMapping("films")
@Getter
public class FilmController {
    private Map<Integer, Film> films = new ConcurrentHashMap<>();
    private int idFilm = 0;

    private void increaseIdFilm() {
        ++idFilm;
    }

    @PostMapping
    public Film createFilm(@Validated @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            log.error("Фильм с таким Id уже существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Фильм уже существует");
        }

        if(StringUtils.isBlank(film.getName())) {
            log.error("Название фильма не должно быть пустым");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Название фильма не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Описание фильма не должно привышать 200 символов");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Описание фильма не должно привышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12, 28 ))) {
            log.debug("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Длительность фильма не должна быть меньше или равна нулю");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Длительность фильма не должна быть меньше или равна нулю");
        }
        increaseIdFilm();
        film.setId(idFilm);
        films.put(idFilm, film);
        log.info("Фильм добавлен в список");

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с таким Id не существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Фильм с таким Id не существует");
        }
        if (StringUtils.isBlank(film.getName())) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Описание фильма не должно привышать 200 символов");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Описание фильма не должно привышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Длительность фильма не должна быть меньше или равна нулю");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Длительность фильма не должна быть меньше или равна нулю");
        }
        log.info("Информация о фильме обновлена");
        films.put(film.getId(), film);

        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Список фильмов");
        return films.values();
    }
}
