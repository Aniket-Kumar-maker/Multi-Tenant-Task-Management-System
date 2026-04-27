package com.aniket.workmgmt.comments;
import com.aniket.workmgmt.activitylogs.ActivityLogService;
import com.aniket.workmgmt.tasks.Task;
import com.aniket.workmgmt.tasks.TaskRepository;
import com.aniket.workmgmt.teams.Team;
import com.aniket.workmgmt.users.Role;
import com.aniket.workmgmt.users.User;
import com.aniket.workmgmt.users.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ActivityLogService activityLogService;

    public CommentService(UserRepository uR, TaskRepository tR, CommentRepository cR, ActivityLogService alS){
        userRepository = uR;
        taskRepository = tR;
        commentRepository = cR;
        activityLogService = alS;
    }

    public Comment createComment(String content, Long userId, Long taskId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found!"));

        if (task.isDeleted()) {
            throw new RuntimeException("Cannot comment on deleted task");
        }

        Team team = task.getTeam();

        if (user.getRole() == Role.CONTRIBUTOR) {
            if (!team.getUsers().contains(user)) {
                throw new RuntimeException("User not part of team!");
            }
        } else {
            if (!team.getOrganization().getId()
                    .equals(user.getOrganization().getId())) {
                throw new RuntimeException("Cannot comment outside user's org!");
            }
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setTask(task);

        Comment savedComment = commentRepository.save(comment);

        String logMessage = String.format(
                "%s commented on task '%s'",
                user.getName(),
                task.getTitle()
        );

        activityLogService.addLog(logMessage, user, task);

        return savedComment;
    }

    public void deleteComment(Long commentId, Long userId){

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!comment.getUser().getId().equals(userId) &&
                user.getRole() == Role.CONTRIBUTOR) {
            throw new RuntimeException("Cannot delete others' comments");
        }

        if(!comment.getUser().getOrganization().getId().equals(user.getOrganization().getId()))
            throw new RuntimeException("Cannot delete comment outside org!");

        commentRepository.delete(comment);
        String logMessage = String.format(
                "%s deleted a comment on task '%s'",
                user.getName(),
                comment.getTask().getTitle()
        );

        activityLogService.addLog(logMessage, user, comment.getTask());
    }

    public List<Comment> getTaskComments(Long taskId){
        return commentRepository.findByTaskId(taskId);
    }

    public Page<Comment> getTaskCommentsPaginated(Long taskId, Pageable pageable){
        return commentRepository.findByTaskId(taskId, pageable);
    }

}
