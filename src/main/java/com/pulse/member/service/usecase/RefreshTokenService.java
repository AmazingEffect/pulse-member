package com.pulse.member.service.usecase;

import com.pulse.member.controller.request.MemberReadRequestDTO;
import com.pulse.member.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(MemberReadRequestDTO memberReadRequestDTO);

    RefreshToken findByToken(String token);

    void deleteByMember(MemberReadRequestDTO memberReadRequestDTO);

    void verifyExpiration(RefreshToken token);

}
