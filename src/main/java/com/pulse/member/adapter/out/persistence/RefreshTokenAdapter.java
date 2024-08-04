package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.repository.RefreshTokenRepository;
import com.pulse.member.application.port.out.refreshtoken.CreateRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.DeleteRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.FindRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.UpdateRefreshTokenPort;
import com.pulse.member.domain.Member;
import com.pulse.member.domain.RefreshToken;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * RefreshTokenAdapter
 */
@RequiredArgsConstructor
@Component
public class RefreshTokenAdapter implements CreateRefreshTokenPort, FindRefreshTokenPort, DeleteRefreshTokenPort, UpdateRefreshTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberMapper memberMapper;


    /**
     * RefreshToken 생성
     *
     * @param refreshToken RefreshToken
     * @return 생성된 RefreshToken
     */
    @Override
    public RefreshToken createRefreshToken(RefreshToken refreshToken) {
        return null;
    }


    /**
     * RefreshToken 삭제
     *
     * @param member 회원
     * @return 삭제 여부
     */
    @Override
    public Boolean deleteRefreshToken(Member member) {
        MemberEntity memberEntity = memberMapper.toEntity(member);
        if (ObjectUtils.isEmpty(memberEntity)) {
            throw new MemberException(ErrorCode.DATA_NOT_FOUND);
        }
        refreshTokenRepository.deleteByMemberEntity(memberEntity);
        return true;
    }


    /**
     * RefreshToken 조회
     *
     * @param refreshToken RefreshToken
     * @return 조회된 RefreshToken
     */
    @Override
    public RefreshToken findRefreshToken(RefreshToken refreshToken) {
        return null;
    }


    /**
     * RefreshToken 갱신
     *
     * @param refreshToken RefreshToken
     * @return 갱신된 RefreshToken
     */
    @Override
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        return null;
    }

}
