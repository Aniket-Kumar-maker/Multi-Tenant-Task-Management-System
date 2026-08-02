package com.aniket.workmgmt.tasks;
import com.aniket.workmgmt.activitylogs.ActivityLogService;
import com.aniket.workmgmt.comments.Comment;
import com.aniket.workmgmt.organizations.Organization;
import com.aniket.workmgmt.teams.Team;
import com.aniket.workmgmt.teams.TeamRepository;
import com.aniket.workmgmt.users.Role;
import com.aniket.workmgmt.users.User;
import com.aniket.workmgmt.users.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public TaskService(TaskRepository taskRep, TeamRepository teamRep, UserRepository userRep, ActivityLogService alS){
        this.taskRepository = taskRep;
        this.teamRepository = teamRep;
        this.userRepository = userRep;
        this.activityLogService = alS;
    }

    public Task createTask(String title, String description, Status status,
                           Priority priority, LocalDate date,
                           Long teamId, Long userId, Long creatorId){

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team Not Found!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found!"));

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Invalid creator!"));

        if (creator.getRole() == Role.CONTRIBUTOR) {
            throw new RuntimeException("Not allowed to create task");
        }

        if (!team.getUsers().contains(user)) {
            throw new RuntimeException("User not in team");
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(date);
        task.setTeam(team);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        String logMessage = String.format(
                "Task '%s' created by %s and assigned to %s in team %s (status: %s, priority: %s)",
                savedTask.getTitle(),
                creator.getName(),
                user.getName(),
                team.getName(),
                status,
                priority
        );

        activityLogService.addLog(logMessage, creator, savedTask);
        return savedTask;
    }

    public Task updateStatus(Long taskId, Long initiatorId, Status status) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found!"));
        User initiator = userRepository.findById(initiatorId).orElseThrow(() -> new RuntimeException("User not found!"));

        if(initiator.getRole() == Role.CONTRIBUTOR){
            if (!task.getUser().equals(initiator)) {
                throw new RuntimeException("Initiator does not own the task!");
            }
        }

        if (task.getStatus() == status) {
            throw new RuntimeException("Status is already " + status);
        }

        Status oldStatus = task.getStatus();
        task.setStatus(status);

        Task savedTask = taskRepository.save(task);

        String logMessage = String.format(
                "%s changed status of task '%s' from %s to %s",
                initiator.getName(),
                task.getTitle(),
                oldStatus,
                status
        );

        activityLogService.addLog(logMessage, initiator, savedTask);
        return savedTask;
    }

    public Task updateTask(Long taskId,
                           String newTitle,
                           String newDescription,
                           Priority newPriority,
                           LocalDate newDueDate,
                           Long initiatorId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task does not exist!"));

        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new RuntimeException("Initiator not found!"));

        if (initiator.getRole() == Role.CONTRIBUTOR) {
            throw new RuntimeException("Not allowed to update task");
        }

        if (newTitle != null) task.setTitle(newTitle);
        if (newDescription != null) task.setDescription(newDescription);
        if (newPriority != null) task.setPriority(newPriority);
        if (newDueDate != null) task.setDueDate(newDueDate);

        Task updatedTask = taskRepository.save(task);

        String logMessage = String.format(
                "%s updated task '%s'",
                initiator.getName(),
                task.getTitle()
        );

        activityLogService.addLog(logMessage, initiator, updatedTask);
        return updatedTask;
    }

    public List<Task> getMyTask(Long userId){
        return taskRepository.findByUserIdAndDeletedFalse(userId);
    }

    public Page<Task> getMyTaskPaginated(Long userId, Pageable pageable){
        return taskRepository.findByAssignedUserIdAndDeletedFalse(userId, pageable);
    }

    public List<Task> getTeamTasks(Long userId, Long teamId){

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found!"));

        if (user.getRole() == Role.CONTRIBUTOR) {
            if (!team.getUsers().contains(user)) {
                throw new RuntimeException("User not in the team!");
            }
        }

        else {
            if (!team.getOrganization().getId()
                    .equals(user.getOrganization().getId())) {
                throw new RuntimeException("Team not in user's organization!");
            }
        }

        return taskRepository.findByTeamIdAndDeletedFalse(teamId);
    }

    public Page<Task> getTeamTasksPaginated(Long userId, Long teamId, Pageable pageable){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found!"));

        if (user.getRole() == Role.CONTRIBUTOR) {
            if (!team.getUsers().contains(user)) {
                throw new RuntimeException("User not in the team!");
            }
        }

        else {
            if (!team.getOrganization().getId()
                    .equals(user.getOrganization().getId())) {
                throw new RuntimeException("Team not in user's organization!");
            }
        }

        return taskRepository.findByTeamIdAndDeletedFalse(teamId,pageable);
    }

    public void deleteTask(Long userId, Long taskId){

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (task.isDeleted()) {
            throw new RuntimeException("Task already deleted");
        }

        if (user.getRole() == Role.CONTRIBUTOR) {
            throw new RuntimeException("Contributor cannot delete task!");
        }

        if (!task.getTeam().getOrganization().getId()
                .equals(user.getOrganization().getId())) {
            throw new RuntimeException("Task outside user's org!");
        }

        task.setDeleted(true);
        taskRepository.save(task);

        String logMessage = String.format(
                "%s deleted task '%s'",
                user.getName(),
                task.getTitle()
        );
        activityLogService.addLog(logMessage, user, task);
    }
}
