package com.pulse.member.application.port.out.refreshtoken;

import com.pulse.member.domain.Member;

public interface DeleteRefreshTokenPort {

    Boolean deleteRefreshToken(Member member);

}
