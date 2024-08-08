package com.pulse.member.mapper;

import com.pulse.member.adapter.out.persistence.entity.MemberOutboxEntity;
import com.pulse.member.domain.MemberOutbox;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberOutboxMapper {

    MemberOutboxEntity domainToEntity(MemberOutbox memberOutbox);

    MemberOutbox entityToDomain(MemberOutboxEntity memberOutboxEntity);

}
