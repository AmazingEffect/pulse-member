package com.pulse.member.controller.response;

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

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseStatus.SUCCESS, "Request was successful", data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ResponseStatus.SUCCESS, message, data, null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(ResponseStatus.FAIL, message, null, null);
    }

    public static <T> ApiResponse<T> fail(String message, List<String> errors) {
        return new ApiResponse<>(ResponseStatus.FAIL, message, null, errors);
    }

}
