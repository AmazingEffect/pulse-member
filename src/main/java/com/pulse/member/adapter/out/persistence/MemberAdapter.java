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


    /**
     * 회원 생성
     *
     * @param member 회원 도메인
     * @return 생성된 회원
     */
    @Override
    public Member createMember(Member member) {
        MemberEntity memberEntity = memberMapper.toEntity(member);
        MemberEntity savedMember = memberRepository.save(memberEntity);
        return memberMapper.toDomain(savedMember);
    }


    /**
     * ID로 회원 조회
     *
     * @param member 회원 도메인
     * @return 조회된 회원
     */
    @Override
    public Member findMemberById(Member member) {
        MemberEntity memberEntity = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toDomain(memberEntity);
    }


    /**
     * Email로 회원 조회
     *
     * @param member 회원 도메인
     * @return 조회된 회원
     */
    @Override
    public Member findMemberByEmail(Member member) {
        MemberEntity memberEntity = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        return memberMapper.toDomain(memberEntity);
    }


    /**
     * 회원 수정
     *
     * @param member 회원 도메인
     * @return 수정된 회원
     */
    @Override
    public Member updateMember(Member member) {
        MemberEntity memberEntity = memberMapper.toEntity(member);
        MemberEntity updatedMember = memberRepository.save(memberEntity);
        return memberMapper.toDomain(updatedMember);
    }


    /**
     * 회원 삭제
     *
     * @param member 회원 도메인
     * @return 삭제 여부
     */
    @Override
    public Boolean deleteMemberById(Member member) {
        memberRepository.deleteById(member.getId());
        return true;
    }

}
