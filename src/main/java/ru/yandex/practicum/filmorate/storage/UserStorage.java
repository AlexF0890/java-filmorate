package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User findById(Integer id);
    User create(User user) throws ValidationException;
    User update(User user) throws ValidationException;
    void remove(User user);
    Collection<User> getUsers();
    void addFriendList(Integer user, Integer friend);
    void removeFriend(Integer user, Integer friend);
    List<User> getMutualFriend(Integer user, Integer friend);
    Boolean isMutualStatus (Integer user, Integer friend);
    List<User> getFriendsList(Integer user);
}
