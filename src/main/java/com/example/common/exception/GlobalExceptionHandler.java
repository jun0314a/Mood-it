package com.example.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1) DB 제약(중복) 예외
    @ExceptionHandler({
        DataIntegrityViolationException.class,
        ConstraintViolationException.class,
        SQLIntegrityConstraintViolationException.class
    })
    public ResponseEntity<Map<String, String>> handleDuplicateUserFields(Exception ex) {
        String message = "이미 사용 중인 정보가 있습니다.";
        String lower = (ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()).toLowerCase();

        if (lower.contains("unique_email")) {
            message = "이미 사용 중인 이메일입니다.";
        } else if (lower.contains("unique_username")) {
            message = "이미 사용 중인 닉네임입니다.";
        } else if (lower.contains("unique_phone")) {
            message = "이미 사용 중인 전화번호입니다.";
        }

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of("message", message));
    }

    // 2) @Valid 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError err = ex.getBindingResult().getFieldErrors().get(0);
        return ResponseEntity
            .badRequest()
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(Map.of("message", err.getDefaultMessage()));
    }

    // 3) IllegalArgumentException (잘못된 인자)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
            .badRequest()
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(Map.of("message", ex.getMessage()));
    }

    // 4) 커스텀 BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(Map.of("message", ex.getMessage()));
    }

    // 5) 기타 RuntimeException (금지어 포함 등)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String msg = ex.getMessage();
        if (msg != null && msg.contains("부적절한 표현")) {
            return ResponseEntity
                .badRequest()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(Map.of("message", msg));
        }
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(Map.of("message", "서버 오류가 발생했습니다."));
    }

    // 6) 그 외 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(Map.of("message", "서버 오류가 발생했습니다."));
    }

    // 7) 지원되지 않는 HTTP 메서드
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotSupported(org.springframework.web.HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(Map.of("message", "지원되지 않는 요청 메서드입니다."));
    }

    // 8) 타입 변환 오류 (예: "me"를 Long으로 변환 시도)
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        String message = "잘못된 요청 파라미터입니다. " + ex.getName() + " 파라미터의 값이 올바르지 않습니다.";
        return ResponseEntity
            .badRequest()
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(Map.of("message", message));
    }

    // 9) 멀티파트 요청 파트 누락
    @ExceptionHandler(org.springframework.web.multipart.support.MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> handleMissingPart(org.springframework.web.multipart.support.MissingServletRequestPartException ex) {
        String message = "필수 요청 파트가 누락되었습니다: " + ex.getRequestPartName();
        return ResponseEntity
            .badRequest()
            .header("Content-Type", "application/json; charset=UTF-8")
            .body(Map.of("message", message));
    }
}
