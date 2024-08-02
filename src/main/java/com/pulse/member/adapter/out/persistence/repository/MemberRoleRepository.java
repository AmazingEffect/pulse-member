package com.pulse.member.adapter.out.persistence.repository;

import com.pulse.member.adapter.out.persistence.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 권한 map 테이블 Repository
 */
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

}