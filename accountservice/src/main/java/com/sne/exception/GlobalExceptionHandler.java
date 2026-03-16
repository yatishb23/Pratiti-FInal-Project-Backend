package com.sne.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.bad_request}")
    private String badRequest;

    @Value("${spring.application.conflict}")
    private String conflict;

    @Value("${spring.application.not_found}")
    private String notFound;

    /**
     * Handles the case when a method argument is not valid.
     *
     * @param ex      The MethodArgumentNotValidException that occurred.
     * @param headers The HttpHeaders of the response.
     * @param status  The HttpStatus of the response.
     * @param request The WebRequest of the response.
     * @return A ResponseEntity with an ErrorResponse and HttpStatus.BAD_REQUEST.
     */
//    @Override
//    public ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return new ResponseEntity<>(new ErrorResponse(badRequest, ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
//    }

    /**
     * Exception handler for GlobalException.
     *
     * @param globalException The GlobalException to handle.
     * @return The ResponseEntity with the error response.
     */
//    @ExceptionHandler(GlobalException.class)
//    public ResponseEntity<Object> handleGlobalException(GlobalException globalException) {
//        return ResponseEntity
//                .badRequest()
//                .body(ErrorResponse.builder()
//                        .errorCode(globalException.getErrorCode())
//                        .message(globalException.getErrorMessage())
//                        .build());
//    }
    
//    @ExceptionHandler(GlobalException.class)
//    public ResponseEntity<Object> handleGlobalException(GlobalException globalException) {
//        
//        // Determine the HTTP Status based on your GlobalErrorCode
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Default
//        
//        if ("404".equals(globalException.getErrorCode())) {
//            status = HttpStatus.NOT_FOUND;
//        } else if ("409".equals(globalException.getErrorCode())) {
//            status = HttpStatus.CONFLICT;
//        } else if ("400".equals(globalException.getErrorCode())) {
//            status = HttpStatus.BAD_REQUEST;
//        }
//
////        return ResponseEntity
////                .status(status) // Use the dynamic status instead of .badRequest()
////                .body(ErrorResponse.builder()
////                        .errorCode(globalException.getErrorCode())
////                        .message(globalException.getErrorMessage())
////                        .build());
//        
//        return new ResponseEntity<>(
//                ErrorResponse.builder()
//                        .errorCode(globalException.getErrorCode())
//                        .message(globalException.getErrorMessage())
//                        .build(), 
//                status // This is the crucial part that Feign reads
//        );
//    }
    
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleGlobalException(GlobalException ex) {
        // 1. Determine Status based on the errorCode string inside your exception
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; 
        
        if ("404".equals(ex.getErrorCode())) {
            status = HttpStatus.NOT_FOUND;
        } else if ("409".equals(ex.getErrorCode())) {
            status = HttpStatus.CONFLICT;
        } else if ("400".equals(ex.getErrorCode())) {
            status = HttpStatus.BAD_REQUEST;
        }

        // 2. Return the response with the CORRECT HTTP Status Code
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getErrorMessage())
                .build(), 
            status // This is what Feign uses to decide which catch block to use!
        );
    }
}
