package com.pulse.member.mapper;

import com.pulse.member.adapter.out.persistence.entity.RefreshTokenEntity;
import com.pulse.member.domain.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RefreshTokenMapper {

    RefreshTokenEntity domainToEntity(RefreshToken refreshToken);

    RefreshToken entityToDomain(RefreshTokenEntity refreshTokenEntity);

}
