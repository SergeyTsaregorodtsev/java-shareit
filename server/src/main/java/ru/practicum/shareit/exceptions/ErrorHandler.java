package ru.practicum.shareit.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        return new ErrorResponse(String.format("Ошибка: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return new ErrorResponse(String.format("Ошибка: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadParameterException(final BadParameterException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        return new ErrorResponse(e.getMessage());
    }
}
