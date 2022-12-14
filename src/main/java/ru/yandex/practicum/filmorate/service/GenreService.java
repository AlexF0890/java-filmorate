package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;

@Service
@Slf4j
@Getter
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public GenreService(@Qualifier("GenreDbStorage") GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre getGenre (Integer id) {
        return genreDbStorage.getGenreId(id);
    }

    public List<Genre> getAllGenre() {
        return genreDbStorage.getGenreAll();
    }
}
