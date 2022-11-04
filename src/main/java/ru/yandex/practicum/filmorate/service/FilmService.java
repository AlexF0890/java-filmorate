package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Getter
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

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



    public Collection<Film> findFilmByCount(Integer count) {
        if(count == null) {
            return inMemoryFilmStorage.getFilms().stream()
                    .sorted((f1, f2) -> {
                        Integer filmOne = f1.getLike().size();
                        Integer filmTwo = f2.getLike().size();
                        return filmOne.compareTo(filmTwo)*-1;
                    })
                    .limit(10)
                    .collect(Collectors.toList());
        } else if (count > 0){
            return inMemoryFilmStorage.getFilms()
                    .stream()
                    .sorted((f1, f2) -> {
                        Integer filmOne = f1.getLike().size();
                        Integer filmTwo = f2.getLike().size();
                        return filmOne.compareTo(filmTwo)*-1;
                    })
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            log.error("Число не может быть отрицательным");
            throw new FilmNotFoundException("Число не может быть отрицательным");
        }
    }
}
