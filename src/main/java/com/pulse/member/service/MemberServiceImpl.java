package com.pulse.member.service;

import com.pulse.member.dto.MemberDTO;
import com.pulse.member.entity.Member;
import com.pulse.member.event.spring.MemberCreateEvent;
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
     * @param id
     * @return
     */
    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        return memberMapper.toDto(member);
    }

    /**
     * 회원 생성 + (이벤트 발행)
     *
     * @param memberDTO
     * @return
     */
    @Override
    @Transactional
    public MemberDTO createMember(MemberDTO memberDTO) {
        Member member = memberMapper.toEntity(memberDTO);
        Member savedMember = memberRepository.saveAndFlush(member);

        // MemberCreateEvent 발행
        eventPublisher.publishEvent(new MemberCreateEvent(savedMember.getId()));
        return memberMapper.toDto(savedMember);
    }

}
