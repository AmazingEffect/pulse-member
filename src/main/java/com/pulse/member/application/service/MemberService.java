package com.pulse.member.application.service;

import com.pulse.member.application.port.in.member.CreateMemberUseCase;
import com.pulse.member.application.port.in.member.DeleteMemberUseCase;
import com.pulse.member.application.port.in.member.FindMemberUseCase;
import com.pulse.member.application.port.in.member.UpdateMemberUseCase;
import com.pulse.member.application.port.out.member.CreateMemberPort;
import com.pulse.member.application.port.out.member.DeleteMemberPort;
import com.pulse.member.application.port.out.member.FindMemberPort;
import com.pulse.member.application.port.out.member.UpdateMemberPort;
import com.pulse.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService implements CreateMemberUseCase, FindMemberUseCase, UpdateMemberUseCase, DeleteMemberUseCase {

    private final CreateMemberPort createMemberPort;
    private final FindMemberPort findMemberPort;
    private final UpdateMemberPort updateMemberPort;
    private final DeleteMemberPort deleteMemberPort;

    private final ApplicationEventPublisher eventPublisher;


    /**
     * 회원 생성
     *
     * @param member 회원
     * @return 생성된 회원
     */
    @Override
    public Member createMember(Member member) {
        member.validMember();
        return createMemberPort.createMember(member);
    }


    /**
     * ID로 회원 조회
     *
     * @param member 회원
     * @return 조회된 회원
     */
    @Override
    public Member findMemberById(Member member) {
        return findMemberPort.findMemberById(member);
    }


    /**
     * Email로 회원 조회
     *
     * @param member 회원
     * @return 조회된 회원
     */
    @Override
    public Member findMemberByEmail(Member member) {
        return findMemberPort.findMemberByEmail(member);
    }


    /**
     * 회원 삭제
     *
     * @param member 회원
     * @return 삭제 여부
     */
    @Override
    public Boolean deleteMemberById(Member member) {
        return deleteMemberPort.deleteMemberById(member);
    }

}
