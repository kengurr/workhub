package com.workhub.controller.advice;

import com.workhub.exception.ApplicationError;
import com.workhub.exception.ServiceProcessingException;
import com.workhub.exception.WorkhubException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity<ApplicationError> handleSPE(ServiceProcessingException cause) {
        ApplicationError applicationError = new ApplicationError();
        applicationError.setCode(cause.getCode());
        applicationError.setType(cause.getType().name());
        applicationError.setMessage(cause.getMessage());

        log.error("Business Exception with cause: ", cause);
        return ResponseEntity.status(cause.getHttp()).body(applicationError);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationError> handleUnhandled(Exception cause) {
        ApplicationError applicationError = new ApplicationError();
        applicationError.setCode(WorkhubException.INTERNAL_SERVER_ERROR.getCode());
        applicationError.setType(WorkhubException.INTERNAL_SERVER_ERROR.getType().name());
        applicationError.setMessage(WorkhubException.INTERNAL_SERVER_ERROR.getMessage());

        log.error("Application Exception with cause: ", cause);
        return ResponseEntity.internalServerError().body(applicationError);
    }
}
