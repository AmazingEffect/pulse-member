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
    MEMBER_ID_NOT_FOUND("MEM001", "Member ID not found"),

    // adapter entity errors
    MEMBER_ENTITY_NOT_FOUND("MEM001", "Member entity not found"),
    ROLE_ENTITY_NOT_FOUND("MEM002", "Role entity not found"),

    // member errors
    MEMBER_REQUIRED_VALUE("MEM001", "Member required value"),
    Not_FOUND_MEMBER_OUTBOX("MEM001", "Not found member outbox"),
    CHANGE_EMAIL_VALUE_NOT_FOUND("MEM001", "Change email value is empty"),
    MEMBER_JWT_ALREADY_EXIST("MEM001", "Member JWT already exist"),
    MEMBER_EMAIL_NOT_MATCH("MEM008", "Member email not match"),
    MEMBER_INNER_EMAIL_NOT_FOUND("MEM009", "Member inner email not found"),
    MEMBER_EMAIL_PARAM_NOT_FOUND("MEM010", "Member email param not found"),
    MEMBER_CREATE_EMAIL_NOT_FOUND("MEM011", "Member create email not found"),
    MEMBER_CREATE_PASSWORD_NOT_FOUND("MEM012", "Member create password not found"),

    // outbox errors
    OUTBOX_STATUS_NOT_FOUND("MEMBOX001", "Outbox status not found"),
    OUTBOX_PROCESSED_AT_NOT_FOUND("MEMBOX002", "Outbox processedAt not found"),
    REFRESH_TOKEN_EXPIRATION_DATE_NOT_FOUND("MEMBOX003", "Refresh token expiration date not found"),
    REFRESH_TOKEN_NOT_EXIST("MEMBOX004", "Refresh token not exist"),


    ;

    private final String code;
    private final String message;

}
