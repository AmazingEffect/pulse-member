package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.repository.MemberRepository;
import com.pulse.member.application.port.out.member.CreateMemberPort;
import com.pulse.member.application.port.out.member.DeleteMemberPort;
import com.pulse.member.application.port.out.member.FindMemberPort;
import com.pulse.member.application.port.out.member.UpdateMemberPort;
import com.pulse.member.domain.Member;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * MemberAdapter
 */
@RequiredArgsConstructor
@Component
public class MemberAdapter implements CreateMemberPort, FindMemberPort, DeleteMemberPort, UpdateMemberPort {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;


    @Override
    public Member createMember(Member member) {
        return null;
    }

    @Override
    public Boolean deleteMemberById(Member member) {
        return null;
    }

    @Override
    public Boolean deleteMemberByEmail(Member member) {
        return null;
    }

    @Override
    public Member findMemberById(Member member) {
        return null;
    }

    @Override
    public Member findMemberByEmail(Member member) {
        MemberEntity memberEntity = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toDomain(memberEntity);
    }

    @Override
    public Member updateMemberById(Member member) {
        return null;
    }

    @Override
    public Member updateMemberByEmail(Member member) {
        return null;
    }

}
