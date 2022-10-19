package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private final String login;
    private String name;
    private int id;
    private final String email;
    private final LocalDate birthday;

}
