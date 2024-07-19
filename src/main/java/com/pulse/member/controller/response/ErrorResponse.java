package com.pulse.member.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String errorCode;
    private String message;
    private List<String> errors;
    private String stackTrace;

    // factory method
    public static ErrorResponse of(String errorCode, String message, List<String> errors, String stackTrace) {
        return new ErrorResponse(errorCode, message, errors, stackTrace);
    }

}
