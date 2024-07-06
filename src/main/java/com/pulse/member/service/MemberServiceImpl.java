package com.pulse.member.service;

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
     * ID로 회원 조회
     *
     * @param id
     * @return
     */
    @Override
    public MemberRetrieveDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        return memberMapper.toRetrieveDto(member);
    }

    /**
     * 회원 생성 + (이벤트 발행)
     *
     * @param memberCreateDTO 회원 정보 DTO
     */
    @Override
    @Transactional
    public MemberCreateDTO createMember(MemberCreateDTO memberCreateDTO) {
        Member member = memberMapper.toEntity(memberCreateDTO);
        Member savedMember = memberRepository.saveAndFlush(member);

        // MemberCreateEvent 발행
        eventPublisher.publishEvent(new MemberCreateEvent(savedMember.getId()));
        return memberMapper.toCreateDto(savedMember);
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

}
