package com.mobile.group.tlu_contact_be.exceptions;

import com.mobile.group.tlu_contact_be.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleBusinessException(CustomException exc) {
        return new ResponseEntity<>(new BaseResponse<>(exc.getData(), exc.getMessage(), exc.getStatus().value()), exc.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBusinessException(MethodArgumentNotValidException exc) {
        return new ResponseEntity<>(new BaseResponse<>(exc.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}
