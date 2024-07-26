package com.pulse.member.service.usecase;

import com.pulse.member.controller.response.MemberReadResponseDTO;
import com.pulse.member.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(MemberReadResponseDTO memberReadResponseDTO);

    RefreshToken findByToken(String token);

    void deleteByMember(MemberReadResponseDTO memberReadResponseDTO);

    void verifyExpiration(RefreshToken token);

}
