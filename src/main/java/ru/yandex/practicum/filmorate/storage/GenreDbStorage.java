package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
@Qualifier("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate filmGenres;

    @Autowired
    public GenreDbStorage(JdbcTemplate filmGenres) {
        this.filmGenres = filmGenres;
    }

    @Override
    public Genre getGenreId(Integer id) {
        try {
            String sqlQuery = "select * from genre where genre_id = ?";
            return filmGenres.queryForObject(sqlQuery, new GenreMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
            throw new GenreNotFoundException("Жанра под таким индетефикатором не существует.");
        }
    }

    @Override
    public List<Genre> getGenreAll() {
        String sqlQuery = "select * from genre";
        return filmGenres.query(sqlQuery, new GenreMapper());
    }

}
