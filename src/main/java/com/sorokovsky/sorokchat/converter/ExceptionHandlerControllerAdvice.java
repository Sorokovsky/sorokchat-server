package com.sorokovsky.sorokchat.converter;

import com.sorokovsky.sorokchat.exception.http.HttpException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerControllerAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            Locale locale
    ) {
        final var message = messageSource.getMessage("error.bad-request", new Object[0], locale);
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        problemDetail.setTitle(message);
        final var errors = exception.getBindingResult().getFieldErrors();
        HashMap<String, String> errorMap = new HashMap<>();
        for (var error : errors) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        problemDetail.setProperty("errors", errorMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ProblemDetail> handleHttpException(HttpException exception, Locale locale) {
        final var message = messageSource.getMessage(exception.getMessage(), new Object[0], locale);
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        problemDetail.setTitle(message);
        return ResponseEntity.status(exception.getHttpStatus()).body(problemDetail);
    }
}
