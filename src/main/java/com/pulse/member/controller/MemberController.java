package com.pulse.member.controller;

import com.pulse.member.controller.request.MemberReadRequestDTO;
import com.pulse.member.controller.response.ApiResponse;
import com.pulse.member.controller.response.MemberReadResponseDTO;
import com.pulse.member.service.usecase.MemberService;
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

    private final MemberService memberService;

    /**
     * @param id 회원 ID
     * @apiNote 회원 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberReadResponseDTO>> getMemberById(@PathVariable Long id) {
        MemberReadResponseDTO memberDTO = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(memberDTO));
    }

}
