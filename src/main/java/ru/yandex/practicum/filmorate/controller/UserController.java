package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequestMapping("users")
@Getter
public class UserController {
    private Map<Integer, User> users = new ConcurrentHashMap<>();
    private int idUser = 0;

    private void increaseIdUser() {
        ++idUser;
    }

    @PostMapping
    public User createUser(@RequestBody final User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            log.error("Пользователь с таким Id существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь уже существует");
        }
        if (StringUtils.isBlank(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ \"@\"");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Электронная почта не может быть пустой и должна содержать символ \"@\"");
        }
        if (StringUtils.isBlank(user.getLogin()) || user.getLogin().contains(" ")) {
            log.error("Логин не должен содержать пробелы и не может быть пустым");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Логин не должен содержать пробелы и не может быть пустым");
        }
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Имя не указано. Имени будет присвои логин.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата рождения не может быть в будущем");
        }
        increaseIdUser();
        user.setId(idUser);
        users.put(idUser, user);
        log.info("Пользователь создан");

        return user;
    }

    @PutMapping
    public User updateUser(@Validated @RequestBody final User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с таким Id не существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь не существует");
        }
        if(StringUtils.isBlank(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть изменена");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Электронная почта не может быть изменена");
        }
        if (StringUtils.isBlank(user.getName()) || user.getName() == null) {
            log.debug("Имя не было измененно или присвоено имя логина");
            user.setName(user.getLogin());
        }
        if (StringUtils.isBlank(user.getLogin()) || user.getLogin().equals(" ")) {
            log.info("Имя не указано. Имени будет присвои логин.");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Логин не был изменен");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть изменена");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата рождения не может быть изменена");
        }
        log.info("Пользователь изменен");
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Список пользователей");
        return users.values();
    }

}