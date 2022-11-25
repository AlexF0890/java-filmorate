package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user;

    /*@BeforeEach
    void setUp() {
        userController = new UserController();
        user = new User("Alex", "Alex3000", 0, "alex@mail.ru", LocalDate.of(1992,11,01));
    }

    @Test
    void createUser() throws ValidationException {
        userController.createUser(user);
        assertEquals(userController.getUsers().size(), 1, "Задача не создалась");

        User userTwo = userController.getUsers().get(1);
        assertAll("Значения не совпадают",
                () -> assertEquals(user.getName(), userTwo.getName()),
                () -> assertEquals(user.getLogin(), userTwo.getLogin()),
                () -> assertEquals(user.getEmail(), userTwo.getEmail()),
                () -> assertEquals(user.getBirthday(), userTwo.getBirthday())
        );
    }


    @Test
    void updateUser() throws ValidationException {
        userController.createUser(user);
        assertEquals(userController.getUsers().size(), 1, "Фильм не завелся");

        User userTwo = new User("Aleksandr","", user.getId(), "alex1845@mail.ru", LocalDate.of(2020,10,12));
        userController.updateUser(userTwo);
        assertEquals(userController.getUsers().size(), 1, "Фильм не завелся");

        User userThree = userController.getUsers().get(1);
        assertAll("Значение не изменилось",
                () -> assertEquals(userTwo.getName(), userThree.getLogin()),
                () -> assertEquals(userTwo.getLogin(), userThree.getLogin()),
                () -> assertEquals(userTwo.getEmail(), userThree.getEmail()),
                () -> assertEquals(userTwo.getBirthday(), userThree.getBirthday())
        );
    }*/
}