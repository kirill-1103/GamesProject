package ru.krey.games.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.krey.games.domain.ExceptionResponse;
import ru.krey.games.error.BadRequestException;


@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @ExceptionHandler(org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException.class)
//    public ResponseEntity<ExceptionResponse> handlerSizeLimitException(org.apache.commons.fileupload
//                                                                               .FileUploadBase.SizeLimitExceededException e) {
//        throw new BadRequestException("Файл слишком большой!", e);
//    }
}
