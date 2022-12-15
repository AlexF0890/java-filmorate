package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException{
    public FilmNotFoundException(String e){
        super(e);
    }
}
