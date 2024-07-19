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
    private List<String> errors;
    private String errorCode;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                ResponseStatus.SUCCESS,
                "Request was successful",
                data,
                null,
                null
        );
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                ResponseStatus.SUCCESS,
                message,
                data,
                null,
                null
        );
    }

    // 커스텀 예외 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(
                ResponseStatus.FAIL,
                errorCode.getMessage(),
                null,
                null,
                errorCode.getCode()
        );
    }

    // 일반적인 예외 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
        return new ApiResponse<>(
                ResponseStatus.FAIL,
                message,
                null,
                null,
                errorCode.getCode()
        );
    }

    // validate 전용 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, List<String> errors) {
        return new ApiResponse<>(
                ResponseStatus.FAIL,
                errorCode.getMessage(),
                null,
                errors,
                errorCode.getCode()
        );
    }

}
