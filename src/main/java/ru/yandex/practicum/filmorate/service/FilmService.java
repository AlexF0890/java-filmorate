package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserDbStorage userDbStorage;
    private final GenreDbStorage genreDbStorage;

    public FilmService (@Qualifier("FilmDbStorage")FilmStorage filmStorage,
                        UserDbStorage userDbStorage, GenreDbStorage genreDbStorage){
        this.filmStorage = filmStorage;
        this.userDbStorage = userDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        filmException(film);
        Film filmNew = filmStorage.createFilm(film);
        log.info("Фильм добавлен");
        return filmNew;
    }
    public Film updateFilm(Film film) throws ValidationException {
        if (film.getId() <= getAllFilms().size()) {
            filmException(film);
            filmStorage.updateFilm(film);
            log.info("Данные о фильме изменены");
            return film;
        } else {
            throw new FilmNotFoundException("Нет фильма");
        }
    }

    public void removeFilm(Film film) {
            filmStorage.removeFilm(film);
    }

    public void addLikeFilm (Integer film, Integer user){
        try {
            filmStorage.addLike(film, user);
            log.info("Лайк поставлен");
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("film_id")) {
                throw new FilmNotFoundException("Фильма с таким id нет");
            } else if (e.getMessage().contains("user_id")) {
                throw new UserNotFoundException("Пользователя с таким id нет");
            }
        }
    }

    public void removeLikeFilm (Integer film, Integer user){
        if (film > 0 && user > 0 && getFilmId(film) != null && userDbStorage.findUserById(user) != null) {
        Set<Integer> like = getFilmId(film).getLike();
        filmStorage.removeLike(film, user);
        like.remove(userDbStorage.findUserById(user).getId());
        log.info("Лайк удален");
        } else {
            throw new FilmNotFoundException("Фильма или пользователя не не существует");
        }
    }

    public Film getFilmId(Integer film){
        if (filmStorage.findFilmById(film) != null) {
            return filmStorage.findFilmById(film);
        } else {
            throw new FilmNotFoundException("Фильма не существует под id = " + film);
        }
    }

    public List<Film> getPopularLikeFilms(Integer count) {
        if (count == null) {
            return filmStorage.getFilmPopular(10);
        }
        return filmStorage.getFilmPopular(count);
    }
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    private void filmException(Film film) throws ValidationException {
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
    }
}
