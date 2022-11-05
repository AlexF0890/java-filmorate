package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public void addFriendList(int idUser, int idFriend){
        if (idUser > 0 && idFriend > 0) {
            User user = inMemoryUserStorage.getUsers().get(idUser - 1);
            if (user != null) {
                User friend = inMemoryUserStorage.getUsers().get(idFriend - 1);
                if (friend != null && friend != user) {
                    log.info("Пользователь добавлен в друзья.");
                    user.getFriends().add(friend.getId());
                    friend.getFriends().add(user.getId());
                } else {
                    log.error("Такого пользователя не существует. Пользователя не добавить в друзья.");
                    throw new UserNotFoundException("Такого пользователя не существует. Пользователя не добавить в друзья.");
                }
            } else {
                log.error("Такого пользователя не существует");
                throw new UserNotFoundException("Такого пользователя не существует");
            }
        } else {
            log.error("Id пользователя должно быть положительным");
            throw new UserNotFoundException("Id пользователя должно быть положительным");
        }
    }

    public void removeFriendList(int idUser, int idFriend){
        if (idUser > 0 && idFriend > 0 && idUser != idFriend) {
            User user = inMemoryUserStorage.getUsers().get(idUser - 1);
            if (user != null) {
                User friend = inMemoryUserStorage.getUsers().get(idFriend - 1);
                if (user.getFriends().contains(friend.getId())) {
                    log.info("Пользователь удален из друзей.");
                    user.getFriends().remove(friend.getId() - 1);
                    friend.getFriends().remove(user.getId() - 1);
                } else {
                    log.error("В списке друзей друг отсутствует или список пуст");
                    throw new UserNotFoundException("В списке друзей друг отсутствует или список пуст");
                }
            } else {
                log.error("Такого пользователя не существует");
                throw new UserNotFoundException("Такого пользователя не существует");
            }
        } else {
            log.error("Id пользователя должно быть положительным");
            throw new UserNotFoundException("Id пользователя должно быть положительным");
        }
    }

    public Set<Integer> getListFriends(int idUser) {
        if (idUser < inMemoryUserStorage.getUsers().size() || idUser > 0) {
            User user = inMemoryUserStorage.getUsers().get(idUser - 1);
            if (user == null) {
                log.error("Такого пользователя не существует");
                throw new UserNotFoundException("Такого пользователя не существует");
            } else {
                return user.getFriends();
            }
        } else {
            log.error("Такого пользователя не существует");
            throw new UserNotFoundException("Такого пользователя не существует");
        }
    }

    public Set<Integer> getMutualFriend(int idUser, int idFriend) {
        Set<Integer> friendsUsers = inMemoryUserStorage.getUsers().get(idUser - 1).getFriends();
        if (friendsUsers == null){
            log.error("Такого пользователя не существует или список друзей пуст");
            throw new UserNotFoundException("Такого пользователя не существует или список друзей пуст");
        }
        Set<Integer> friendsFriends = inMemoryUserStorage.getUsers().get(idFriend - 1).getFriends();
        if (friendsFriends == null){
            log.error("Такого пользователя не существует или список друзей пуст");
            throw new UserNotFoundException("Такого пользователя не существует или список друзей пуст");
        }
        Set<Integer> generalUsers = new HashSet<>(friendsUsers);
        generalUsers.retainAll(friendsFriends);
        return generalUsers;
    }

    public Set<User> showNumbersOfFriends(int id, int otherId) {
        Set<User> friends = new HashSet<>();
        Set<Integer> friendsId = getMutualFriend(id, otherId);
        for (int idUser: friendsId) {
            friends.add(inMemoryUserStorage.getUserId(idUser));
        }
        return friends;
    }

    public Set<User> showAllFriends(int id) {
        Set<User> friends = new HashSet<>();
        Set<Integer> friendsId = getListFriends(id);
        for (int idUser: friendsId) {
            friends.add(inMemoryUserStorage.getUserId(idUser));
        }
        return friends;
    }

    public User findUserId(int id){
        return inMemoryUserStorage.getUserId(id);
    }

    public Collection<User> findAll() {
        log.info("Список пользователей");
        return inMemoryUserStorage.getUsers();
    }

    public User addUser(User user) throws ValidationException {
        return inMemoryUserStorage.createUser(user);
    }

    public User putUser(User user) throws ValidationException {
        return inMemoryUserStorage.updateUser(user);
    }

    public void removeUser(User user){
        inMemoryUserStorage.removeUser(user);
    }
}
