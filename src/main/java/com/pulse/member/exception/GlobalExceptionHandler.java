package com.pulse.member.exception;

import com.pulse.member.controller.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 전역적으로 발생하는 예외를 처리하는 클래스
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        ApiResponse<String> response = ApiResponse.fail(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Spring Boot 애플리케이션에서 데이터 유효성 검사 중에 발생하는 제약 조건 위반 예외를 처리하기 위해 사용됩니다.
     * 이 예외는 주로 Java Bean Validation (JSR 380) 어노테이션(예: @NotNull, @Size, @Min 등)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        // 1. 제약 조건 위반 메시지를 수집합니다.
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        
        // 2. ApiResponse 객체를 생성하여 응답합니다.
        ApiResponse<String> response = ApiResponse.fail("Validation error", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * 커스텀 예외인 MemberException을 처리하기 위해 사용됩니다.
     */
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberException(MemberException e) {
        ApiResponse<String> response = ApiResponse.fail(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
