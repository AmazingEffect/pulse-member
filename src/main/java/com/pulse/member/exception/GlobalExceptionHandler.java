package com.pulse.member.exception;

import com.pulse.member.adapter.in.web.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 전역적으로 발생하는 예외를 처리하는 클래스
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AppProperties appProperties;

    /**
     * 커스텀 예외인 MemberException을 처리하기 위해 사용됩니다.
     */
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberException(MemberException e) {
        String stackTrace = appProperties.isIncludeStackTrace() ? ExceptionUtils.getStackTrace(e) : null;
        ApiResponse<String> response = ApiResponse.fail(e.getErrorCode(), stackTrace);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * 애플리케이션에서 발생하는 모든 예외를 처리하기 위해 사용됩니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        String stackTrace = appProperties.isIncludeStackTrace() ? ExceptionUtils.getStackTrace(e) : null;
        ApiResponse<String> response = ApiResponse.fail(ErrorCode.UNEXPECTED_ERROR, e.getMessage(), stackTrace);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Spring Boot 애플리케이션에서 데이터 유효성 검사 중에 발생하는 제약 조건 위반 예외를 처리하기 위해 사용됩니다.
     * 이 예외는 주로 Java Bean Validation (JSR 380) 어노테이션(예: @NotNull, @Size, @Min 등)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        ApiResponse<String> response = ApiResponse.fail(ErrorCode.VALIDATION_FAILED, errors, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
