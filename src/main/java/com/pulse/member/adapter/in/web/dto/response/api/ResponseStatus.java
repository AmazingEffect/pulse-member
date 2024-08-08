package com.pulse.member.adapter.in.web.dto.response.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API 응답 상태 code
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseStatus {

    SUCCESS("200", "Request was successful"),
    FAIL("400", "Request was failed");

    private String code;
    private String description;

}
