package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;
    public Mpa getMpa (Integer mpa) {
        return mpaDbStorage.getId(mpa);
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAll();
    }
}