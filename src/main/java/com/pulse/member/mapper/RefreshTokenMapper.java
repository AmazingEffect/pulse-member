package com.pulse.member.mapper;

import com.pulse.member.adapter.out.persistence.entity.RefreshTokenEntity;
import com.pulse.member.domain.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MemberMapper.class} // memberMapper를 사용합니다.
)
public interface RefreshTokenMapper {

    @Mapping(target = "memberEntity", source = "member")
    RefreshTokenEntity domainToEntity(RefreshToken refreshToken);

    RefreshToken entityToDomain(RefreshTokenEntity refreshTokenEntity);

}
