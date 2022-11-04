package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Getter
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> films = new ArrayList<>();
    private int idFilm = 0;

    public InMemoryFilmStorage() {
    }

    private void increaseIdFilm() {
        ++idFilm;
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        if (films.contains(film)) {
            log.error("Фильм с таким Id уже существует");
            throw new FilmNotFoundException("Фильм уже существует");
        }
        if (StringUtils.isBlank(film.getName())) {
            log.error("Название фильма не должно быть пустым");
            throw new ValidationException("Название фильма не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Описание фильма не должно привышать 200 символов");
            throw new ValidationException("Описание фильма не должно привышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12, 28 ))) {
            log.debug("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Длительность фильма не должна быть меньше или равна нулю");
            throw new ValidationException("Длительность фильма не должна быть меньше или равна нулю");
        }
        increaseIdFilm();
        film.setId(idFilm);
        films.add(film);
        log.info("Фильм добавлен в список");

        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (films.size() < film.getId()) {
            log.error("Фильм не существует");
            throw new FilmNotFoundException("Фильм уже существует");
        }
        if (StringUtils.isBlank(film.getName())) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Описание фильма не должно привышать 200 символов");
            throw new ValidationException("Описание фильма не должно привышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Длительность фильма не должна быть меньше или равна нулю");
            throw new ValidationException("Длительность фильма не должна быть меньше или равна нулю");
        }
        log.info("Информация о фильме обновлена");
        films.set((film.getId() - 1), film);
        return film;
    }

    @Override
    public void removeFilm(Film film) {
        if (!films.contains(film)) {
            log.error("Фильм с таким Id не существует");
            throw new FilmNotFoundException("Фильм с таким Id не существует");
        } else {
            log.info("Фильм удален");
            films.remove(film);
        }
    }

    public Film getFilmId(int idFilm) {
        if (films.size() > idFilm) {
            return films.get(idFilm - 1);
        } else {
            log.error("Такого фильма нет");
            throw new FilmNotFoundException("Такого фильма нет");
        }
    }
}
