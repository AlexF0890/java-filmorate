package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User createUser(User user) throws ValidationException;
    User updateUser(User user) throws ValidationException;
    void removeUser(User user);
}
