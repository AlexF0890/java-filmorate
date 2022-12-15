package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.*;

@Data
@ToString(of = "id")
public class User {
    private int id;
    private String login;
    private String name;
    private String email;
    private LocalDate birthday;
    private  List<Integer> friends = new ArrayList<>();

    public User (int id, String login, String name, String email, LocalDate birthday){
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}
