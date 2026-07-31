package com.aniket.workmgmt.tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserIdAndDeletedFalse(Long userId);
    List<Task> findByTeamIdAndDeletedFalse(Long teamId);
    Page<Task> findByAssignedUserIdAndDeletedFalse(Long userId, Pageable pageable);
    Page<Task> findByTeamIdAndDeletedFalse(Long teamId, Pageable pageable);
}
