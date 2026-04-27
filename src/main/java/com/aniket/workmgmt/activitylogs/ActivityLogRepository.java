package com.aniket.workmgmt.activitylogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog,Long> {
    List<ActivityLog> findByTaskId(Long taskId);
    List<ActivityLog> findByUserId(Long userId);
    Page<ActivityLog> findByTaskId(Long taskId, Pageable pageable);
    Page<ActivityLog> findByUserId(Long userId, Pageable pageable);

}
