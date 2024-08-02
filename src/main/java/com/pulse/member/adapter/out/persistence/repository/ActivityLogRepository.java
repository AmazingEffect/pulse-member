package com.pulse.member.adapter.out.persistence.repository;

import com.pulse.member.adapter.out.persistence.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

}
