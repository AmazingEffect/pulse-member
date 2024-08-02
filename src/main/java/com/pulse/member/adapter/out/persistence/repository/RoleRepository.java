package com.pulse.member.adapter.out.persistence.repository;

import com.pulse.member.adapter.out.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 회원 권한 Repository
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String roleUser);

}
