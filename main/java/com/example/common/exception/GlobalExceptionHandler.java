package com.example.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.hibernate.exception.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //이메일 중복 처리
    @ExceptionHandler({
        DataIntegrityViolationException.class,
        ConstraintViolationException.class,
        SQLIntegrityConstraintViolationException.class
    })
    public ResponseEntity<?> handleDuplicateUserFields(Exception ex) {
        String message = "이미 사용 중인 정보가 있습니다.";
        String lowerMessage = ex.getMessage().toLowerCase();
    
        if (lowerMessage.contains("unique_email")) {
            message = "이미 사용 중인 이메일입니다.";
        } else if (lowerMessage.contains("unique_username")) {
            message = "이미 사용 중인 닉네임입니다.";
        } else if (lowerMessage.contains("unique_phone")) {
            message = "이미 사용 중인 전화번호입니다.";
        }
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of("message", message));
    }

    // 유효성 검사 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put("message", error.getDefaultMessage()); // 프론트에서 읽기 쉽게 "message" 키 사용
            break; // 하나만 보여줄 경우 (가장 첫 번째 에러 메시지)
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 그 외 예외 처리 (선택)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "서버 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
