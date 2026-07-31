package com.aniket.workmgmt.activitylogs;
import com.aniket.workmgmt.tasks.Task;
import com.aniket.workmgmt.teams.Team;
import com.aniket.workmgmt.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository alR){
        activityLogRepository = alR;
    }

    public void addLog(String content, User actor, Task task){
        ActivityLog log = new ActivityLog();

        log.setContent(content);
        log.setUser(actor);        // who performed action
        log.setTask(task);
        if(task!=null) {
            log.setTeam(task.getTeam()); // derive automatically
        }
        log.setCreatedAt(LocalDateTime.now());
        activityLogRepository.save(log);
    }

    public List<ActivityLog> getLogsByTask(Long taskId){
        return activityLogRepository.findByTaskId(taskId);
    }

    public List<ActivityLog> getLogsByUser(Long userId){
        return activityLogRepository.findByUserId(userId);
    }

    public Page<ActivityLog> getLogsByTaskPaginated(Long taskId, Pageable pageable){
        return activityLogRepository.findByTaskId(taskId, pageable);
    }

    public Page<ActivityLog> getLogsByUserPaginated(Long userId, Pageable pageable){
        return activityLogRepository.findByUserId(userId, pageable);
    }

}
