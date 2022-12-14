package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    private final UserDbStorage userDbStorage;

    private final User user = new User(1, "Login", "Name", "email@mail.ru",
            LocalDate.of(1955, 12,12));

    @Test
    void addUser() {
        userDbStorage.createUser(user);
        assertEquals(user, userDbStorage.findUserById(user.getId()));
        assertEquals(1, userDbStorage.getUsersAll().size());

        User user2 = new User(2, "Login2", "Name2", "email2@mail.ru",
                LocalDate.of(1978, 11,11));
        userDbStorage.createUser(user2);
        assertEquals(2, userDbStorage.getUsersAll().size());

        userDbStorage.removeUser(user);
        assertEquals(1, userDbStorage.getUsersAll().size());

        userDbStorage.removeUser(user2);
    }

    @Test
    void updateUser() {
        userDbStorage.createUser(user);
        user.setName("NameUpdate");
        userDbStorage.updateUser(user);
        assertEquals("NameUpdate", userDbStorage.findUserById(user.getId()).getName());

        userDbStorage.removeUser(user);
    }

    @Test
    void addNullName() {
        user.setName(null);
        userDbStorage.createUser(user);
        assertEquals("Login", userDbStorage.findUserById(user.getId()).getName());

        userDbStorage.removeUser(user);
    }

    @Test
    void getFriendsUser() {
        userDbStorage.createUser(user);
        User user2 = new User(2, "Login2", "Name2", "email2@mail.ru",
                LocalDate.of(1978, 11,11));
        userDbStorage.createUser(user2);
        userDbStorage.addFriendList(user.getId(), user2.getId());
        assertEquals(false, userDbStorage.isMutualStatus(1,2));
        assertEquals(user2, userDbStorage.getFriendsList(user.getId()).get(0));
        userDbStorage.addFriendList(user2.getId(), user.getId());

        assertEquals(user, userDbStorage.getFriendsList(user2.getId()).get(0));
        assertEquals(true, userDbStorage.isMutualStatus(user2.getId(),user.getId()));

        assertEquals(1, userDbStorage.getFriendsList(user.getId()).size());
        assertEquals("email2@mail.ru", userDbStorage.getFriendsList(user.getId()).get(0).getEmail());
    }
}
