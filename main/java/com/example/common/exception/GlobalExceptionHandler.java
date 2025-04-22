package com.example.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    // 금지어 포함 시 따로 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String msg = ex.getMessage();
    
        // 금지어 필터에 걸린 경우
        if (msg != null && msg.contains("부적절한 표현")) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("message", msg));
        }

        // 그 외 일반 오류
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "서버 오류가 발생했습니다."));
    }

        // 커스텀 BadRequestException 처리
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());  // ✅ 한글 메시지 그대로 전달
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(error);
    }

    // 그 외 예외 처리 (선택)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "서버 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
