package com.pulse.member.service;

import com.pulse.member.dto.MemberDTO;
import com.pulse.member.entity.Member;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        return memberMapper.toDto(member);
    }

    public MemberDTO createMember(MemberDTO memberDTO) {
        Member member = memberMapper.toEntity(memberDTO);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toDto(savedMember);
    }

}
