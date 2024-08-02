package com.pulse.member.adapter.in.web;

import com.pulse.member.adapter.in.web.dto.response.ApiResponse;
import com.pulse.member.adapter.in.web.dto.response.MemberReadResponseDTO;
import com.pulse.member.application.port.in.MemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberUseCase memberUseCase;

    /**
     * @param id 회원 ID
     * @apiNote 회원 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberReadResponseDTO>> getMemberById(@PathVariable Long id) {
        MemberReadResponseDTO memberDTO = memberUseCase.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(memberDTO));
    }

    // todo: 유저정보 변경

    // todo: 비밀번호 변경

    // todo: 아이디 찾기 (등록된 이메일 찾기)

    // todo: 비밀번호 찾기

    // todo: 휴대폰 인증방법

}
