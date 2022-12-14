package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User findUserById(Integer id);
    User createUser(User user) throws ValidationException;
    User updateUser(User user) throws ValidationException;
    void removeUser(User user);
    Collection<User> getUsersAll();
    void addFriendList(Integer user, Integer friend);
    void removeFriend(Integer user, Integer friend);
    List<User> getMutualFriend(Integer user, Integer friend);
    Boolean isMutualStatus (Integer user, Integer friend);
    List<User> getFriendsList(Integer user);
}
