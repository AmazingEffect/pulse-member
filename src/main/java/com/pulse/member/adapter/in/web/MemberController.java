package com.pulse.member.adapter.in.web;

import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.api.ApiResponse;
import com.pulse.member.application.query.FindMemberQuery;
import com.pulse.member.application.port.in.member.FindMemberUseCase;
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

    private final FindMemberUseCase findMemberUseCase;

    /**
     * @param memberId 회원 ID
     * @return 회원 정보 응답 DTO
     * @apiNote 회원 정보 조회
     */
    @GetMapping("/info/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> getMemberInfo(
            @PathVariable Long memberId
    ) {
        FindMemberQuery command = FindMemberQuery.of(memberId);
        MemberResponseDTO responseDTO = findMemberUseCase.findMember(command);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    // todo: 유저정보 변경

    // todo: 비밀번호 변경

    // todo: 아이디 찾기 (등록된 이메일 찾기)

    // todo: 비밀번호 찾기

    // todo: 휴대폰 인증방법

}
