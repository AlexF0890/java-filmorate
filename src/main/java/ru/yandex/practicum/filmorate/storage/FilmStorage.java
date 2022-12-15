package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film findById(Integer id);
    Film create(Film film) throws ValidationException;
    Film update(Film film) throws ValidationException;
    void remove(Film film);
    Collection<Film> getAllFilms();
    void addLike(Integer film, Integer user);
    void removeLike(Integer film, Integer user);
    List<Film> getFilmPopular(Integer count);
    List<Integer> getFilmLikeUser(Integer film);
    Set<Genre> getFilmGenre(Integer film);
    void deleteGenre(Film film);
}
