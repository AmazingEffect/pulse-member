package com.pulse.member.adapter.out.persistence.repository;

import com.pulse.member.adapter.out.persistence.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 권한 map 테이블 Repository
 */
public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, Long> {

}
