package com.rohan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEmployeeNotFound(UsernameNotFoundException ex) {
        return ex.getMessage();
    }
	@ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIncorrectPassword(IncorrectPasswordException ex) {
        return ex.getMessage();
    }
	@ExceptionHandler(UnableToUpdateProfileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleProfileUpdate(UnableToUpdateProfileException ex) {
        return ex.getMessage();
    }
	@ExceptionHandler(AccountNotActiveException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccountNotActiveException(AccountNotActiveException ex) {
        return ex.getMessage();
    }
	@ExceptionHandler(UsernameNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUsernameNotAvailableException(UsernameNotAvailableException ex) {
        return ex.getMessage();
    }
}
