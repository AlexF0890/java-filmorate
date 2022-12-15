package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
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
        userDbStorage.create(user);
        assertEquals(user, userDbStorage.findById(user.getId()));
        assertEquals(1, userDbStorage.getUsers().size());

        User user2 = new User(2, "Login2", "Name2", "email2@mail.ru",
                LocalDate.of(1978, 11,11));
        userDbStorage.create(user2);
        assertEquals(2, userDbStorage.getUsers().size());

        userDbStorage.remove(user);
        assertEquals(1, userDbStorage.getUsers().size());

        userDbStorage.remove(user2);
        assertEquals(0, userDbStorage.getUsers().size());
    }

    @Test
    void updateUser() {
        userDbStorage.create(user);
        user.setName("NameUpdate");
        userDbStorage.update(user);
        assertEquals("NameUpdate", userDbStorage.findById(user.getId()).getName());

        userDbStorage.remove(user);
    }

    @Test
    void addNullName() {
        user.setName(null);
        userDbStorage.create(user);
        assertEquals("Login", userDbStorage.findById(user.getId()).getName());

        userDbStorage.remove(user);
    }

    @Test
    void getFriendsUser() {
        userDbStorage.create(user);
        User user2 = new User(2, "Login2", "Name2", "email2@mail.ru",
                LocalDate.of(1978, 11,11));
        userDbStorage.create(user2);
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
