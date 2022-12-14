package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Integer, User> users = new HashMap<>();

    private int idUser = 0;

    public InMemoryUserStorage(){
    }

    private void increaseIdUser() {
        ++idUser;
    }

    @Override
    public User findUserById(Integer id) {
        if (users.get(id) != null && id > 0) {
            return users.get(id);
        } else {
            throw new UserNotFoundException("Пользователь не существует");
        }
    }

    @Override
    public User createUser(User user) throws ValidationException {
        if (users.get(user.getId()) != null) {
            log.error("Пользователь уже существует");
            throw new UserNotFoundException("Пользователь уже существует");
        }
        if (StringUtils.isBlank(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ \"@\"");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ \"@\"");
        }
        if (StringUtils.isBlank(user.getLogin()) || user.getLogin().contains(" ")) {
            log.error("Логин не должен содержать пробелы и не может быть пустым");
            throw new ValidationException("Логин не должен содержать пробелы и не может быть пустым");
        }
        if (user.getName() == null || user.getName().equals("")) {
            log.info("Имя не указано. Имени будет присвои логин.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        increaseIdUser();
        user.setId(idUser);
        users.put(idUser, user);
        log.info("Пользователь создан");

        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        if (users.get(user.getId()) == null || user.getId() <= 0) {
            log.error("Пользователь не существует");
            throw new UserNotFoundException("Пользователь не существует");
        }
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
        log.info("Пользователь изменен");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void removeUser(User user) {
        users.remove(user);
        log.info("Пользователь удален");
    }

    @Override
    public Collection<User> getUsersAll() {
        return users.values();
    }

    @Override
    public void addFriendList(Integer user, Integer friend) {

    }

    @Override
    public void removeFriend(Integer user, Integer friend) {

    }

    @Override
    public List<User> getMutualFriend(Integer user, Integer friend) {
        return null;
    }

    @Override
    public Boolean isMutualStatus(Integer user, Integer friend) {
        return null;
    }

    @Override
    public List<User> getFriendsList(Integer user) {
        return null;
    }

    public User getUserId(int idUser) {
        if (users.size() < idUser || idUser <= 0){
            log.error("Такого пользователя нет");
            throw new UserNotFoundException("Такого пользователя нет");
        } else {
            log.info("Пользователь существует");
            return users.get(idUser-1);
        }
    }
}