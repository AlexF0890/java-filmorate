package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@Slf4j
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService){
        this.genreService = genreService;
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable("id") Integer id){
        return genreService.getGenre(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenreAll(){
        return genreService.getAllGenre();
    }
}
