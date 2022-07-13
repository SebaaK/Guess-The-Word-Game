package kots.controller;

import kots.controller.dto.ResponseMessageDto;
import kots.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class GlobalHttpErrorHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ResponseMessageDto> incorrectFileTypeException(ObjectNotFoundException exception) {
        return new ResponseEntity<>(new ResponseMessageDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
