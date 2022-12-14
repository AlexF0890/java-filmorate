package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;

@Service
@Slf4j
@Getter
public class MpaService {
    private final MpaDbStorage mpaDbStorage1;

    public MpaService(@Qualifier("MpaDbStorage") MpaDbStorage mpaDbStorage1) {
        this.mpaDbStorage1 = mpaDbStorage1;
    }

    public Mpa getMpa (Integer mpa) {
        return mpaDbStorage1.getMpaId(mpa);
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage1.getMpaAll();
    }
}