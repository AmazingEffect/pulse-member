package com.pulse.member.repository;

import com.pulse.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 권한 map 테이블 Repository
 */
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

}
