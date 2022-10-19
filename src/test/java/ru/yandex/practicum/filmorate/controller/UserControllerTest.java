package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user = new User("Alex", "Alex3000", 0, "alex@mail.ru", LocalDate.of(1992,11,01));
    }

    @Test
    void createUser() {
        userController.createUser(user);
        assertEquals(userController.getUsers().size(), 1, "Задача не создалась");
    }

    @Test
    void updateUser() {
        userController.createUser(user);
        assertEquals(userController.getUsers().size(), 1, "Фильм не завелся");
        assertAll("Значение не изменилось",
                () -> assertEquals(user.getName(), userController.getUsers().get(1).getName()),
                () -> assertEquals(user.getLogin(), userController.getUsers().get(1).getLogin()),
                () -> assertEquals(user.getEmail(), userController.getUsers().get(1).getEmail()),
                () -> assertEquals(user.getBirthday(), userController.getUsers().get(1).getBirthday())
        );

        User userTwo = new User("Aleksandr","alex1845@mail.ru", user.getId(), "alex1845@mail.ru", LocalDate.of(2020,10,12));
        userController.updateUser(userTwo);
        assertEquals(userController.getUsers().size(), 1, "Фильм не завелся");
        assertAll("Значение не изменилось",
                () -> assertEquals(userTwo.getEmail(), userController.getUsers().get(1).getEmail()),
                () -> assertEquals(userTwo.getLogin(), userController.getUsers().get(1).getLogin()),
                () -> assertEquals(userTwo.getName(), userController.getUsers().get(1).getLogin()),
                () -> assertEquals(userTwo.getBirthday(), userController.getUsers().get(1).getBirthday())
        );
    }
}