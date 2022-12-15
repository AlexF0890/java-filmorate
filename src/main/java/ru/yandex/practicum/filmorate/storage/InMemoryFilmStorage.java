package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int idFilm = 0;

    public InMemoryFilmStorage(){}

    private void increaseIdFilm() {
        ++idFilm;
    }

    @Override
    public Film findById(Integer id) {
        if (films.get(id) != null) {
            getFilmGenre(id);
            return films.get(id);
        } else {
            log.error("Такого фильма нет");
            throw new FilmNotFoundException("Такого фильма нет");
        }
    }

    @Override
    public Film create(Film film) throws ValidationException {
        if (films.get(film.getId()) != null) {
            log.error("Фильм с таким Id уже существует");
            throw new FilmNotFoundException("Фильм уже существует1");
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
        films.put(idFilm, film);
        log.info("Фильм добавлен в список");

        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        if (films.get(film.getId()) == null) {
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
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void remove(Film film) {
        if (films.get(film.getId()) == null) {
            log.error("Фильм с таким Id не существует");
            throw new FilmNotFoundException("Фильм с таким Id не существует");
        } else {
            log.info("Фильм удален");
            films.remove(film);
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void addLike(Integer film, Integer user) {
        films.get(film).getLike().add(user);
    }

    @Override
    public void removeLike(Integer film, Integer user) {
        films.get(film).getLike().remove(user);
    }

    @Override
    public List<Film> getFilmPopular(Integer count) {
        if(count == null) {
            return sortedList(10);
        } else if (count > 0){
            return sortedList(count);
        } else {
            log.error("Число не может быть отрицательным");
            throw new FilmNotFoundException("Число не может быть отрицательным");
        }
    }

    @Override
    public List<Integer> getFilmLikeUser(Integer film) {
        Set<Integer> likes = films.get(film).getLike();
        return new ArrayList<>(likes);
    }

    @Override
    public Set<Genre> getFilmGenre(Integer film) {
        return null;
    }


    @Override
    public void deleteGenre(Film film) {
        List<Genre> genres = film.getGenres();
        genres.clear();
    }

    private List<Film> sortedList(Integer count){
        List<Film> filmsList = new ArrayList<>(films.values());
        return filmsList.stream()
                .sorted((f1, f2) -> {
                    Integer filmOne = f1.getLike().size();
                    Integer filmTwo = f2.getLike().size();
                    return filmOne.compareTo(filmTwo)*-1;
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}