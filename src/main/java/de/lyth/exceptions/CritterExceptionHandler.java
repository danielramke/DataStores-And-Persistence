package de.lyth.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class CritterExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public void entityNotFoundException(EntityNotFoundException exception, HttpServletResponse response) throws IOException {
        log.error(exception.getMessage());
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler
    public void entityExistsException(EntityExistsException exception, HttpServletResponse response) throws IOException {
        log.error(exception.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @ExceptionHandler
    public void validationException(ValidationException exception, HttpServletResponse response) throws IOException {
        log.error(exception.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

}
