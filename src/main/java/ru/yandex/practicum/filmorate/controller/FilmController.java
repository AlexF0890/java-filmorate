package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping("/films/popular")
    public List<Film> showNumbersOfLike(@RequestParam(value = "count", required = false) Integer count) {
        return filmService.getPopularLikeFilms(count);
    }

    @GetMapping("/films/{id}")
    public Film findFilmId(@PathVariable("id") Integer id) {
        return filmService.getFilmId(id);
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.getAllFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addFilmLike(@PathVariable("id") Integer id,
                            @PathVariable("userId") Integer userId) {
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeFilmLike(@PathVariable("id") Integer id,
                            @PathVariable("userId") Integer userId) {
        filmService.removeLikeFilm(id, userId);
    }

    @PostMapping("/films")
    public Film addFilms(@RequestBody Film film) throws ValidationException {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilms(@RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/films")
    public void removeFilms(@RequestBody Film film){
        filmService.removeFilm(film);
    }
}
