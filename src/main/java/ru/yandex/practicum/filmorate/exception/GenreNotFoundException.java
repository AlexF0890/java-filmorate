package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException{
    public GenreNotFoundException(String e){
            super(e);
        }
}
