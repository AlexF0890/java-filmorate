package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Service
@Slf4j
@Getter
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    @Autowired
    private final InMemoryUserStorage inMemoryUserStorage;

    public void addLikeFilm (int idFilm, int userId) {
        if (idFilm > 0 && userId > 0) {
            Film film = inMemoryFilmStorage.getFilms().get(idFilm - 1);
            if (film != null) {
                User user = inMemoryUserStorage.getUsers().get(userId-1);
                if (user != null) {
                    log.info("Пользователь поставил лайк");
                    film.getLike().add(user.getId());
                } else {
                    log.error("Такого пользователя не существует");
                    throw new UserNotFoundException("Такого пользователя не существует");
                }
            } else {
                log.error("Такого фильма не существует");
                throw new FilmNotFoundException("Такого фильма не существует");
            }
        } else {
            log.error("Число не может быть отрицательным");
            throw new FilmNotFoundException("Число не может быть отрицательным");
        }
    }

    public void removeLikeFilm (int idFilm, int userId) {
        if (idFilm > 0 && userId > 0) {
            Film film = inMemoryFilmStorage.getFilms().get(idFilm - 1);
            if (film != null) {
                User user = inMemoryUserStorage.getUsers().get(userId-1);
                if (user != null) {
                    log.info("Пользователь удалил лайк");
                    film.getLike().remove(user.getId());
                } else {
                    log.error("Такого пользователя не существует");
                    throw new UserNotFoundException("Такого пользователя не существует");
                }
            } else {
                log.error("Такого фильма не существует");
                throw new FilmNotFoundException("Такого фильма не существует");
            }
        } else {
            log.error("Число не может быть отрицательным");
            throw new FilmNotFoundException("Число не может быть отрицательным");
        }
    }



    public List<Film> findFilmByCount(Integer count) {
        if(count == null) {
            return inMemoryFilmStorage.sortedList(10);
        } else if (count > 0){
            return inMemoryFilmStorage.sortedList(count);
        } else {
            log.error("Число не может быть отрицательным");
            throw new FilmNotFoundException("Число не может быть отрицательным");
        }
    }

    public Film findFilmId(int id) {
        return inMemoryFilmStorage.getFilmId(id);
    }

    public List<Film> findAll() {
        return inMemoryFilmStorage.getFilms();
    }

    public Film addFilms(Film film) throws ValidationException {
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilms(Film film) throws ValidationException {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public void removeFilms(Film film) {
        inMemoryFilmStorage.removeFilm(film);
    }
}
