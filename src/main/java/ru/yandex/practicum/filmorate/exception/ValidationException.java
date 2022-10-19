package ru.yandex.practicum.filmorate.exception;

import org.springframework.validation.annotation.Validated;

@Validated
public class ValidationException extends RuntimeException {

    public ValidationException (String e) {
        super(e);
    }
}
