package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping("/films/popular")
    public Collection<Film> showNumbersOfLike(@RequestParam(value = "count", required = false) Integer count) {
            return filmService.findFilmByCount(count);
    }

    @GetMapping("/films/{id}")
    public Film findFilmId(@PathVariable("id") int id) {
        return filmService.findFilmId(id);
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addFilmLike(@PathVariable("id") int id,
                            @PathVariable("userId") int userId) {
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeFilmLike(@PathVariable("id") int id,
                            @PathVariable("userId") int userId) {
        filmService.removeLikeFilm(id, userId);
    }

    @PostMapping("/films")
    public Film addFilms(@RequestBody Film film) throws ValidationException {
        return filmService.addFilms(film);
    }

    @PutMapping("/films")
    public Film updateFilms(@RequestBody Film film) throws ValidationException {
        return filmService.updateFilms(film);
    }

    @DeleteMapping("/films")
    public void removeFilms(@RequestBody Film film) throws ValidationException {
        filmService.removeFilms(film);
    }
}
