package org.example.dayleaf.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(
            CustomException e,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(
            Exception e,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        request.getRequestURI()
                ));
    }
}