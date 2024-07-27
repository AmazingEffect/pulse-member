package com.pulse.member.service;

import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.response.MemberReadResponseDTO;
import com.pulse.member.entity.Member;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.listener.spring.event.NicknameChangeEvent;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.repository.MemberRepository;
import com.pulse.member.service.usecase.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * ID로 회원 조회
     *
     * @param id 회원 ID
     * @return 회원 조회 DTO
     */
    @Override
    public MemberReadResponseDTO getMemberById(Long id) {
        return memberRepository.findById(id)
                .map(memberMapper::toReadDto)
                .orElseThrow(() -> new RuntimeException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }


    /**
     * 이메일로 회원 조회
     *
     * @param email 이메일
     * @return 회원 조회 DTO
     */
    @Override
    public MemberReadResponseDTO getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(memberMapper::toReadDto)
                .orElseThrow(() -> new RuntimeException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }


    /**
     * 닉네임 변경
     *
     * @param id          회원 ID
     * @param newNickname 변경할 닉네임
     */
    @Transactional
    @Override
    public Long changeNickname(Long id, String newNickname) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        member.changeNickname(newNickname);
        memberRepository.save(member);

        // NicknameChangeEvent 발행
        eventPublisher.publishEvent(new NicknameChangeEvent(member.getId()));

        return 1L;
    }


    @Transactional
    @Override
    public void logout(LogoutRequestDTO logoutRequest) {

    }

}
