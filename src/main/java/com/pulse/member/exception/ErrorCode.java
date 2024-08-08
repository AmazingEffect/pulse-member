package com.pulse.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // authentication errors
    MEMBER_NOT_FOUND("MEM001", "Member not found"),
    MEMBER_ALREADY_EXISTS("MEM002", "User already exists"),
    INVALID_USER_ID("MEM003", "Invalid user ID"),
    INVALID_USER_NAME("MEM004", "Invalid user name"),
    INVALID_USER_EMAIL("MEM005", "Invalid user email"),
    INVALID_USER_PHONE("MEM006", "Invalid user phone"),

    // Validation errors
    VALIDATION_FAILED("MEM100", "Validation failed"),

    // Internal errors
    INTERNAL_SERVER_ERROR("MEM500", "Internal server error"),
    UNEXPECTED_ERROR("MEM999", "Unexpected error"),
    REFRESH_TOKEN_EXPIRED("MEM997", "Refresh token expired"),
    TOKEN_NOT_FOUND("MEM998", "Token not found"),

    UNAUTHORIZED("MEM401", "Unauthorized"),

    // ENUM errors
    INVALID_MESSAGE_STATUS("MEM555", "Invalid MessageStatus code"),
    INVALID_ROLE_NAME("MEM556", "Invalid RoleName code"),
    DATA_NOT_FOUND("MEM557", "Data not found"),
    REFRESH_TOKEN_NOT_HAVE_MEMBER("MEM998", "Refresh token does not have a member"),
    INVALID_INPUT_VALUE("MEM400", "Invalid input value"),
    MEMBER_ID_NOT_FOUND("MEM001", "Member ID not found")

    ;

    private final String code;
    private final String message;

}
