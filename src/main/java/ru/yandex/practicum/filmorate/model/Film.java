package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film{
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private final Set<Integer> like = new HashSet<>();
    private List<Genre> genres;
    private MPA mpa;

    public Film (int id, String name, String description, LocalDate releaseDate, int duration){
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}