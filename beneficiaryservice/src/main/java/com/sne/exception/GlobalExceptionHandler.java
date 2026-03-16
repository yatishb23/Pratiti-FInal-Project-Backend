package com.sne.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sne.dto.response.Response;

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
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Response> handleValidationErrors(MethodArgumentNotValidException ex) {
//        // Extract the message you wrote in the @Pattern(message = "...")
//        String errorMsg = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
//
//        Response response = Response.builder()
//                .responseCode("400")
//                .message(errorMsg)
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {

        // 1. Extract your custom message (e.g., from the IFSC @Pattern)
        String errorMsg = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        // 2. Wrap it in your standard Response DTO
        Response response = Response.builder()
                .responseCode("400")
                .message(errorMsg)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleGlobalException(GlobalException globalException) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .errorCode(globalException.getErrorCode())
                        .message(globalException.getErrorMessage())
                        .build());
    }
}
