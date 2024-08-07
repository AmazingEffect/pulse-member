package com.pulse.member.mapper;

import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.domain.Jwt;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JwtMapper {

    JwtResponseDTO domainToResponseDTO(Jwt jwt);

}
