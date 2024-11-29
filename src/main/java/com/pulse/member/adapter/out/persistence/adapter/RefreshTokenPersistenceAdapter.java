package com.pulse.member.adapter.out.persistence.adapter;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.entity.RefreshTokenEntity;
import com.pulse.member.adapter.out.persistence.repository.RefreshTokenRepository;
import com.pulse.member.application.port.out.refreshtoken.CreateRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.DeleteRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.FindRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.UpdateRefreshTokenPort;
import com.pulse.member.common.annotation.PersistenceAdapter;
import com.pulse.member.domain.Member;
import com.pulse.member.domain.RefreshToken;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@PersistenceAdapter
public class RefreshTokenPersistenceAdapter implements CreateRefreshTokenPort, FindRefreshTokenPort, DeleteRefreshTokenPort, UpdateRefreshTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberMapper memberMapper;
    private final RefreshTokenMapper refreshTokenMapper;


    /**
     * @param refreshToken RefreshToken
     * @return 생성된 RefreshToken
     * @apiNote RefreshToken 생성
     */
    @Override
    public RefreshToken createRefreshToken(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.domainToEntity(refreshToken);
        validRefreshTokenEntity(refreshTokenEntity);
        // RefreshToken 생성 및 저장
        RefreshTokenEntity savedRefreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenMapper.entityToDomain(savedRefreshTokenEntity);
    }


    /**
     * @param member 회원
     * @return 삭제 여부
     * @apiNote RefreshToken 삭제
     */
    @Override
    public Boolean deleteRefreshToken(Member member) {
        MemberEntity memberEntity = memberMapper.toEntity(member);
        validMemberEntity(memberEntity);
        // 회원의 RefreshToken 삭제
        refreshTokenRepository.deleteByMemberEntity(memberEntity);
        return true;
    }


    /**
     * @param refreshToken RefreshToken
     * @return 조회된 RefreshToken
     * @apiNote RefreshToken 조회
     */
    @Override
    public RefreshToken findRefreshToken(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.domainToEntity(refreshToken);
        validRefreshTokenEntity(refreshTokenEntity);

        // 이메일을 통해 RefreshToken 조회
        String email = refreshTokenEntity.getMemberEntity().getEmail();
        RefreshTokenEntity findRefreshTokenEntity = refreshTokenRepository.findRefreshTokenEntityByMemberEntity_Email(email)
                .orElseThrow(() -> new MemberException(ErrorCode.DATA_NOT_FOUND));

        return refreshTokenMapper.entityToDomain(findRefreshTokenEntity);
    }


    /**
     * @param refreshToken RefreshToken
     * @return 갱신된 RefreshToken
     * @apiNote RefreshToken 갱신
     */
    @Override
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        return null;
    }


    /**
     * @param refreshTokenEntity RefreshTokenEntity
     * @apiNote RefreshTokenEntity 유효성 검사
     */
    private void validRefreshTokenEntity(RefreshTokenEntity refreshTokenEntity) {
        if (ObjectUtils.isEmpty(refreshTokenEntity)) throw new MemberException(ErrorCode.DATA_NOT_FOUND);
    }


    /**
     * @param memberEntity MemberEntity
     * @apiNote MemberEntity 유효성 검사
     */
    private void validMemberEntity(MemberEntity memberEntity) {
        if (ObjectUtils.isEmpty(memberEntity)) throw new MemberException(ErrorCode.DATA_NOT_FOUND);
    }

}
