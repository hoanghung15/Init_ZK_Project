package org.example.staff_module.exception;

import org.example.staff_module.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse> handleUserExistedException(RuntimeException ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(400)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
