package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> showNumbersOfFriends(@PathVariable("id") int id,
                                          @PathVariable("otherId") int otherId) {
        Set<User> friends = new HashSet<>();
        Set<Integer> friendsId = userService.getMutualFriend(id, otherId);
        for (int idUser: friendsId) {
            friends.add(userService.getInMemoryUserStorage().getUserId(idUser));
        }
        return friends;
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> showAllFriends(@PathVariable("id") int id)  {
        Set<User> friends = new HashSet<>();
        Set<Integer> friendsId = userService.getListFriends(id);
        for (int idUser: friendsId) {
            friends.add(userService.getInMemoryUserStorage().getUserId(idUser));
        }
        return friends;
    }


    @GetMapping("/users/{id}")
    public User findUserId(@PathVariable("id") int id) {
        return userService.getInMemoryUserStorage().getUserId(id);
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.getInMemoryUserStorage().getUsers();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id,
                               @PathVariable("friendId") int friendId) {
        userService.addFriendList(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int id,
                                  @PathVariable("friendId") int friendId) {
        userService.removeFriendList(id, friendId);
    }

    @PostMapping("/users")
    public User addUser (@RequestBody User user) throws ValidationException {
        return userService.getInMemoryUserStorage().createUser(user);
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidationException {
        return userService.getInMemoryUserStorage().updateUser(user);
    }

    @DeleteMapping("/users")
    public void removeUser(@RequestBody User user){
        userService.getInMemoryUserStorage().removeUser(user);
    }
}