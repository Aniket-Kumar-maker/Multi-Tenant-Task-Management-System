package com.aniket.workmgmt.mapper;

import com.aniket.workmgmt.activitylogs.ActivityLog;
import com.aniket.workmgmt.dto.ActivityLogResponse;

public class ActivityLogMapper {

    public static ActivityLogResponse toDto(ActivityLog log){
        ActivityLogResponse dto = new ActivityLogResponse();

        dto.setId(log.getId());
        dto.setContent(log.getContent());
        dto.setUserName(log.getUser().getName());

        if (log.getTask() != null) {
            dto.setTaskId(log.getTask().getId());
        }

        dto.setCreatedAt(log.getCreatedAt());

        return dto;
    }
}