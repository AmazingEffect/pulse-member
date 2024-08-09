package com.pulse.member.application.port.out.role.map;

import com.pulse.member.domain.Member;
import com.pulse.member.domain.Role;

public interface CreateMemberRolePort {

    Long createMemberRole(Member savedMember, Role findRole);

}
