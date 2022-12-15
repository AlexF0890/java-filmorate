package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> showNumbersOfFriends(@PathVariable("id") Integer id,
                                           @PathVariable("otherId") Integer otherId) {
        return userService.getMutualFriendList(id, otherId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> showAllFriends(@PathVariable("id") Integer id)  {
        return userService.getFriendList(id);
    }

    @GetMapping("/users/{id}")
    public User findUserId(@PathVariable("id") Integer id) {
        return userService.getUserId(id);
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.getAllUsers();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id,
                               @PathVariable("friendId") Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Integer id,
                                  @PathVariable("friendId") Integer friendId) {
        userService.removeFriend(id, friendId);
    }

    @PostMapping("/users")
    public User addUser (@RequestBody User user) throws ValidationException {
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @DeleteMapping("/users")
    public void removeUser(@RequestBody User user){
        userService.removeUser(user);
    }
}