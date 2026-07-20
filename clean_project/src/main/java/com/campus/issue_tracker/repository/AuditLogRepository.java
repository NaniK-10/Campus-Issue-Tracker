package com.campus.issue_tracker.repository;

import com.campus.issue_tracker.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Adapted to use relatedId/relatedType instead of entityId/entityType
    List<AuditLog> findByRelatedIdAndRelatedTypeOrderByTimestampDesc(Long relatedId, String relatedType);

    // Adapted to use username instead of performedBy
    List<AuditLog> findByUsernameOrderByTimestampDesc(String username);
}
