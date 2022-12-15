package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate filmGenres;
    private final GenreMapper genreMapper;

    @Override
    public Genre getId(Integer id) {
        try {
            String sqlQuery = "select * from genre where genre_id = ?";
            return filmGenres.queryForObject(sqlQuery, genreMapper, id);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
            throw new GenreNotFoundException("Жанра под таким индетефикатором не существует.");
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from genre";
        return filmGenres.query(sqlQuery, genreMapper);
    }

}
