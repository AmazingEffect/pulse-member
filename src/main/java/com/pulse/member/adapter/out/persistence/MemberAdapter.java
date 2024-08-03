package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.repository.MemberRepository;
import com.pulse.member.application.port.out.member.CreateMemberPort;
import com.pulse.member.application.port.out.member.DeleteMemberPort;
import com.pulse.member.application.port.out.member.FindMemberPort;
import com.pulse.member.application.port.out.member.UpdateMemberPort;
import com.pulse.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * MemberAdapter
 */
@RequiredArgsConstructor
@Component
public class MemberAdapter implements CreateMemberPort, FindMemberPort, DeleteMemberPort, UpdateMemberPort {

    private final MemberRepository memberRepository;

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
        return null;
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
