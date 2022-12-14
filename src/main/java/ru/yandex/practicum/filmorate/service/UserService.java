package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserDbStorage userDbStorage;

    public UserService (@Qualifier("UserDbStorage") UserDbStorage userDbStorage){
        this.userDbStorage = userDbStorage;
    }

    public User addUser(User user) throws ValidationException {
        usersException(user);
        return userDbStorage.createUser(user);
    }

    public void removeUser(User user){
        userDbStorage.removeUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        usersException(user);
        return userDbStorage.updateUser(user);
    }

    public List<User> getAllUsers(){
        return userDbStorage.getUsersAll();
    }

    public User getUserId (Integer user) {
        return userDbStorage.findUserById(user);
    }

    public void addFriend(Integer user, Integer friend){
        if (user > 0 && friend > 0 && getUserId(user) != null && getUserId(friend) != null) {
            userDbStorage.addFriendList(user, friend);
            getUserId(user).getFriends().add(getUserId(friend).getId());
        } else {
            throw new UserNotFoundException("Нет пользователей");
        }
    }

    public void removeFriend(Integer user, Integer friend){
        if (user > 0 && friend > 0 && getUserId(user) != null && getUserId(friend) != null) {
            userDbStorage.removeFriend(user, friend);
        } else {
            throw new UserNotFoundException("Нет пользователей");
        }
    }

    public List<User> getFriendList(Integer user){
        return userDbStorage.getFriendsList(user);
    }

    public List<User> getMutualFriendList(Integer user, Integer friend) {
            return userDbStorage.getMutualFriend(user, friend);
    }

    public void usersException (User user) throws ValidationException {
        if(StringUtils.isBlank(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть изменена");
            throw new ValidationException("Электронная почта не может быть изменена");
        }
        if (StringUtils.isBlank(user.getName()) || user.getName() == null) {
            log.debug("Имя не было измененно или присвоено имя логина");
            user.setName(user.getLogin());
        }
        if (StringUtils.isBlank(user.getLogin()) || user.getLogin().equals(" ")) {
            log.info("Имя не указано. Имени будет присвои логин.");
            throw new ValidationException("Логин не был изменен");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть изменена");
            throw new ValidationException("Дата рождения не может быть изменена");
        }
    }
}
