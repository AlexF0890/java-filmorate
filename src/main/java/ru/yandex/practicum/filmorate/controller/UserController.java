package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
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
        return userService.showNumbersOfFriends(id, otherId);
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> showAllFriends(@PathVariable("id") int id)  {
        return userService.showAllFriends(id);
    }

    @GetMapping("/users/{id}")
    public User findUserId(@PathVariable("id") int id) {
        return userService.findUserId(id);
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.findAll();
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
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidationException {
        return userService.putUser(user);
    }

    @DeleteMapping("/users")
    public void removeUser(@RequestBody User user){
        userService.removeUser(user);
    }
}