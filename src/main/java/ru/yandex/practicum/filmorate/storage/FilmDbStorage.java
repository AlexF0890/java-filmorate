package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.function.UnaryOperator.identity;

@Repository
@Slf4j
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage){
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public Film findFilmById(Integer id) {
        try {
            String sqlQuery = "select f.*, m.* " +
                    "from films as f " +
                    "join mpa as m on f.mpa_id = m.mpa_id " +
                    "where f.film_id = ?";
            Film film = jdbcTemplate.queryForObject(sqlQuery, new FilmMapper(), id);
            if (film != null) {
                film.setGenres(getFilmGenres(film.getId()));
            }
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("Фильма не существует");
        }
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO films (film_name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId((keyHolder.getKey()).intValue());
        film.setMpa(mpaDbStorage.getMpaId(film.getMpa().getId()));
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Genre> genres = new TreeSet<>((g1, g2) -> g1.getId() - g2.getId());
            genres.addAll(film.getGenres());
            film.setGenres(new ArrayList<>(genres));
            String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, film.getId());
                    ps.setLong(2, film.getGenres().get(i).getId());
                }
                public int getBatchSize() {
                    return film.getGenres().size();
                }
            });
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set " +
                "film_name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteGenre(film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Genre> genres = new TreeSet<>((g1, g2) -> g1.getId() - g2.getId());
            genres.addAll(film.getGenres());
            film.setGenres(new ArrayList<>(genres));
            String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, film.getId());
                    ps.setLong(2, film.getGenres().get(i).getId());
                }

                public int getBatchSize() {
                    return film.getGenres().size();
                }
            });
        }
        film.setMpa(mpaDbStorage.getMpaId(film.getMpa().getId()));
        return film;
    }

    @Override
    public void removeFilm(Film film) {
        String sqlQuery = "delete from films where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select * from films as f " +
                "join mpa as m on m.mpa_id = f.mpa_id";
        List<Film> films = jdbcTemplate.query(sqlQuery, new FilmMapper());
        addGenres(films);
        return films;
    }

    private void addGenres(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        String sqlQuery = "select * from genre as g, film_genres as fg where fg.genre_id = g.genre_id AND fg.film_id in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.setGenres(new ArrayList<>());
            film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        }, films.stream().map(Film::getId).toArray());
    }

    @Override
    public void addLike(Integer film, Integer user) {
        String sqlQuery = "insert into likes(film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, film, user);
    }

    @Override
    public void removeLike(Integer film, Integer user) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, film, user);
    }

    @Override
    public List<Film> getFilmPopular(Integer count) {
        String sqlQuery = "select films.*, mpa.*, count(likes.film_id) as rate " +
                "from films " +
                "left join mpa on films.mpa_id = mpa.mpa_id " +
                "left join likes on films.film_id = likes.film_id " +
                "group by films.film_id " +
                "order by rate desc, films.film_id " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, new FilmMapper(), count);
    }

    @Override
    public List<Integer> getFilmLikeUser(Integer film) {
        String sqlQuery = "select user_id from likes where film_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, film);
    }

    @Override
    public Set<Genre> getFilmGenre(Integer film) {
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(format("select fg.genre_id, g.genre_name " +
                "from film_genres AS fg " +
                "left inner join genre as g on fg.genre_id = g.genre_id " +
                "where fg.film_id = ? " +
                "order by g.genre_id", film), new GenreMapper()));
        return genres;
    }

    public void deleteGenre(Film film) {
        String sqlQuery = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    public List<Genre> getFilmGenres(Integer film) {
        String genreSql = "SELECT fg.genre_id, g.genre_name " +
                "FROM film_genres as fg " +
                "JOIN genre as g ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(genreSql, new GenreMapper(), film);
    }
}
