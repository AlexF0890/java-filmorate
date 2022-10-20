package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

@Validated
public class ValidationException extends Exception {

    public ValidationException (HttpStatus badRequest, String e) {
        super(e);
    }
}
