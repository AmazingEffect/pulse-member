package com.pulse.member.service;

import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.dto.MemberCreateDTO;
import com.pulse.member.dto.MemberRetrieveDTO;
import com.pulse.member.entity.Member;
import com.pulse.member.listener.spring.event.MemberCreateEvent;
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
     * 회원 생성 + (이벤트 발행)
     *
     * @param signUpRequestDTO 회원 가입 요청 DTO
     */
    @Transactional
    @Override
    public MemberSignUpRequestDTO register(MemberSignUpRequestDTO signUpRequestDTO) {
        Member member = memberMapper.toEntity(signUpRequestDTO);
        Member savedMember = memberRepository.saveAndFlush(member);

        // MemberCreateEvent 발행
        eventPublisher.publishEvent(new MemberCreateEvent(savedMember.getId()));
        return memberMapper.toCreateDto(savedMember);
    }


    /**
     * ID로 회원 조회
     *
     * @param id 회원 ID
     * @return 회원 조회 DTO
     */
    @Override
    public MemberRetrieveDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        return memberMapper.toRetrieveDto(member);
    }


    /**
     * 닉네임 변경
     *
     * @param id          회원 ID
     * @param newNickname 변경할 닉네임
     */
    @Override
    public Long changeNickname(Long id, String newNickname) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        member.changeNickname(newNickname);
        memberRepository.save(member);

        // NicknameChangeEvent 발행
        eventPublisher.publishEvent(new NicknameChangeEvent(member.getId()));

        return 1L;
    }


    @Override
    public void logout(LogoutRequestDTO logoutRequest) {

    }

}
