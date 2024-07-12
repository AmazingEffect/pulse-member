package com.pulse.member.controller;

import com.pulse.member.controller.request.MemberRetrieveDTO;
import com.pulse.member.service.usecase.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public MemberRetrieveDTO getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }

}
