package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    private String login;
    private String name;
    private String email;

    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();

    public User (int id, String login, String name, String email, LocalDate birthday){
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}
