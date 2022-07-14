package kots.controller;

import kots.controller.dto.ResponseMessageDto;
import kots.exception.NoFileException;
import kots.exception.ObjectNotFoundException;
import kots.exception.WordNameIsExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
class GlobalHttpErrorHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ResponseMessageDto> incorrectFileTypeException(ObjectNotFoundException exception) {
        return new ResponseEntity<>(new ResponseMessageDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoFileException.class)
    public ResponseEntity<ResponseMessageDto> handleNoFileException(NoFileException exception) {
        return new ResponseEntity<>(new ResponseMessageDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WordNameIsExistException.class)
    public ResponseEntity<ResponseMessageDto> handleWordNameIsExistException(WordNameIsExistException exception) {
        return new ResponseEntity<>(new ResponseMessageDto(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessageDto> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return new ResponseEntity<>(new ResponseMessageDto("File size is too large. Max size: " + exception.getMaxUploadSize()), HttpStatus.NOT_ACCEPTABLE);
    }
}
