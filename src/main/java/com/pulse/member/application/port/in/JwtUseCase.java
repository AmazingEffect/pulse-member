package com.pulse.member.application.port.in;

import com.pulse.member.adapter.in.web.dto.response.MemberReadResponseDTO;
import com.pulse.member.adapter.out.persistence.entity.RefreshTokenEntity;

public interface JwtUseCase {

    RefreshTokenEntity createRefreshToken(MemberReadResponseDTO memberReadResponseDTO);

    RefreshTokenEntity findByToken(String token);

    void deleteByMember(MemberReadResponseDTO memberReadResponseDTO);

    void verifyExpiration(RefreshTokenEntity token);

}
