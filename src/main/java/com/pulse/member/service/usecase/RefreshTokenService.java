package com.pulse.member.service.usecase;

import com.pulse.member.controller.request.MemberRetrieveDTO;
import com.pulse.member.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(MemberRetrieveDTO memberRetrieveDTO);

    RefreshToken findByToken(String token);

    void deleteByMember(MemberRetrieveDTO memberRetrieveDTO);

    void verifyExpiration(RefreshToken token);

}
