package com.aniket.workmgmt.tasks;

import com.aniket.workmgmt.dto.PageResponse;
import com.aniket.workmgmt.dto.TaskResponse;
import com.aniket.workmgmt.mapper.TaskMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }


    @PostMapping
    public TaskResponse createTask(@RequestBody TaskCreateRequest request){
        return TaskMapper.toDto(
                taskService.createTask(
                        request.getTitle(),
                        request.getDescription(),
                        request.getStatus(),
                        request.getPriority(),
                        request.getDueDate(),
                        request.getTeamId(),
                        request.getAssignedUserId(),
                        request.getCreatorId()
                )
        );
    }

    @PutMapping("/{taskId}")
    public TaskResponse updateTask(@PathVariable Long taskId,
                                   @RequestBody TaskUpdateRequest request){
        return TaskMapper.toDto(
                taskService.updateTask(
                        taskId,
                        request.getTitle(),
                        request.getDescription(),
                        request.getPriority(),
                        request.getDueDate(),
                        request.getInitiatorId()
                )
        );
    }


    @PatchMapping("/{taskId}/status")
    public TaskResponse updateStatus(@PathVariable Long taskId,
                                     @RequestBody StatusUpdateRequest request){
        return TaskMapper.toDto(
                taskService.updateStatus(
                        taskId,
                        request.getInitiatorId(),
                        request.getStatus()
                )
        );
    }

    @GetMapping("/my")
    public PageResponse<TaskResponse> getMyTasks(@RequestParam Long userId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<Task> taskPage = taskService.getMyTaskPaginated(userId,pageable);

        List<TaskResponse> content = taskPage.stream()
                .map(TaskMapper::toDto)
                .toList();

        PageResponse<TaskResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(taskPage.getNumber());
        response.setSize(taskPage.getSize());
        response.setTotalElements(taskPage.getTotalElements());
        response.setTotalPages(taskPage.getTotalPages());
        return response;
    }


    @GetMapping("/team")
    public PageResponse<TaskResponse> getTeamTasks(
            @RequestParam Long userId,
            @RequestParam Long teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<Task> taskPage = taskService.getTeamTasksPaginated(userId, teamId, pageable);

        List<TaskResponse> content = taskPage.stream()
                .map(TaskMapper::toDto)
                .toList();

        PageResponse<TaskResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(taskPage.getNumber());
        response.setSize(taskPage.getSize());
        response.setTotalElements(taskPage.getTotalElements());
        response.setTotalPages(taskPage.getTotalPages());
        return response;
    }

    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable Long taskId,
                             @RequestParam Long userId){
        taskService.deleteTask(userId, taskId);
        return "Task deleted successfully";
    }
}