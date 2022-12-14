package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@Qualifier("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate mpa;

    @Autowired
    public MpaDbStorage(JdbcTemplate mpa) {
        this.mpa = mpa;
    }

    @Override
    public Mpa getMpaId(Integer id) {
        try {
            String sqlQuery = "select * from mpa where mpa_id = ?";
            List<Mpa> mpaList = mpa.query(sqlQuery, new MpaMapper(), id);
            if (mpaList.size() > 0) {
                return mpaList.get(0);
            } else {
                throw new MpaNotFoundException("Такого рейтинга нет");
            }
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
            throw new MpaNotFoundException("Такого рейтинга нет.");
        }

    }

    @Override
    public List<Mpa> getMpaAll() {
        String sqlQuery = "select * from mpa";
        return mpa.query(sqlQuery, new MpaMapper());
    }
}
