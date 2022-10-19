package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("users")
@Getter
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int idUser = 0;

    private void increaseIdUser() {
        ++idUser;
    }

    @PostMapping
    public User createUser(@RequestBody final User user) {
        if (users.containsKey(user.getId())) {
            log.error("Пользователь с таким Id существует");
            throw new ValidationException("Пользователь уже существует");
        } else if(user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ \"@\"");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ \"@\"");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не должен содержать пробелы и не может быть пустым");
            throw new ValidationException("Логин не должен содержать пробелы и не может быть пустым");
        } else if (user.getName().isBlank() || user.getName().equals("")) {
            log.info("Имя не указано. Имени будет присвои логин.");
            user.setName(user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        increaseIdUser();
        user.setId(idUser);
        users.put(idUser, user);
        log.info("Пользователь создан");
        return user;
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody final User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с таким Id не существует");
            throw new ValidationException("Пользователь не существует");
        } else if(user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть изменена");
            throw new ValidationException("Электронная почта не может быть изменена");
        } else if (user.getName().isBlank() || user.getName() == null) {
            log.debug("Имя не было измененно или присвоено имя логина");
            user.setName(user.getLogin());
        } else if (user.getLogin().isBlank() || user.getLogin().equals("")) {
            log.info("Имя не указано. Имени будет присвои логин.");
            throw new ValidationException("Логин не был изменен");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть изменена");
            throw new ValidationException("Дата рождения не может быть изменена");
        }

        users.put(user.getId(), user);
        log.info("Пользователь изменен");
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Список пользователей");
        return users.values();
    }

}