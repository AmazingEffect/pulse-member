package com.pulse.member.controller.response;

import com.pulse.member.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private ResponseStatus success;
    private String message;
    private T data;
    private ErrorResponse error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                ResponseStatus.SUCCESS,
                "Request was successful",
                data,
                null
        );
    }


    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                ResponseStatus.SUCCESS,
                message,
                data,
                null
        );
    }


    // 커스텀 예외 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String stackTrace) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null, stackTrace);

        return new ApiResponse<>(
                ResponseStatus.FAIL,
                errorCode.getMessage(),
                null,
                errorResponse
        );
    }


    // 일반적인 예외 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message, String stackTrace) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), message, null, stackTrace);

        return new ApiResponse<>(
                ResponseStatus.FAIL,
                message,
                null,
                errorResponse
        );
    }


    // validate 전용 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, List<String> errors, String stackTrace) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), errors, stackTrace);

        return new ApiResponse<>(
                ResponseStatus.FAIL,
                errorCode.getMessage(),
                null,
                errorResponse
        );
    }

}
