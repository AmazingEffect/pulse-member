package com.pulse.member.application.port.in;

import com.pulse.member.adapter.in.web.dto.response.MemberReadResponseDTO;
import com.pulse.member.adapter.out.persistence.entity.RefreshToken;

public interface JwtUseCase {

    RefreshToken createRefreshToken(MemberReadResponseDTO memberReadResponseDTO);

    RefreshToken findByToken(String token);

    void deleteByMember(MemberReadResponseDTO memberReadResponseDTO);

    void verifyExpiration(RefreshToken token);

}
