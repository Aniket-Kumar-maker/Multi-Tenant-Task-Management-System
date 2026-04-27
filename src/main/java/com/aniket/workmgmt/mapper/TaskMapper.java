package com.aniket.workmgmt.mapper;

import com.aniket.workmgmt.dto.TaskResponse;
import com.aniket.workmgmt.tasks.Task;

public class TaskMapper {

    public static TaskResponse toDto(Task task){
        TaskResponse dto = new TaskResponse();

        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        dto.setPriority(task.getPriority().name());
        dto.setDueDate(task.getDueDate());

        dto.setTeamName(task.getTeam().getName());
        dto.setAssignedUserName(task.getUser().getName());

        return dto;
    }
}