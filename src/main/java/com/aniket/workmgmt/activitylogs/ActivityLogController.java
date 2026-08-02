package com.aniket.workmgmt.activitylogs;

import com.aniket.workmgmt.dto.ActivityLogResponse;
import com.aniket.workmgmt.dto.PageResponse;
import com.aniket.workmgmt.mapper.ActivityLogMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService asL){
        this.activityLogService = asL;
    }



    @GetMapping("/tasks/{taskId}/activity")
    public PageResponse<ActivityLogResponse> getLogsTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<ActivityLog> logPage =
                activityLogService.getLogsByTaskPaginated(taskId, pageable);

        List<ActivityLogResponse> content = logPage.stream()
                .map(ActivityLogMapper::toDto)
                .toList();

        PageResponse<ActivityLogResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(logPage.getNumber());
        response.setSize(logPage.getSize());
        response.setTotalElements(logPage.getTotalElements());
        response.setTotalPages(logPage.getTotalPages());

        return response;
    }


    @GetMapping("/users/{userId}/activity")
    public PageResponse<ActivityLogResponse> getLogsUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<ActivityLog> logPage =
                activityLogService.getLogsByUserPaginated(userId, pageable);

        List<ActivityLogResponse> content = logPage.stream()
                .map(ActivityLogMapper::toDto)
                .toList();

        PageResponse<ActivityLogResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(logPage.getNumber());
        response.setSize(logPage.getSize());
        response.setTotalElements(logPage.getTotalElements());
        response.setTotalPages(logPage.getTotalPages());
        return response;
    }
}