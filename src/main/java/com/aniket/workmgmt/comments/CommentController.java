package com.aniket.workmgmt.comments;

import com.aniket.workmgmt.dto.CommentResponse;
import com.aniket.workmgmt.dto.PageResponse;
import com.aniket.workmgmt.mapper.CommentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponse createComment(@RequestBody CommentCreateRequest request){
        return CommentMapper.toDto(
                commentService.createComment(
                        request.getContent(),
                        request.getUserId(),
                        request.getTaskId()
                )
        );
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId,
                                @RequestParam Long userId){
        commentService.deleteComment(commentId, userId);
        return "Comment deleted successfully";
    }

    @GetMapping("/task/{taskId}")
    public PageResponse<CommentResponse> getCommentsByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> commentPage =
                commentService.getTaskCommentsPaginated(taskId, pageable);

        List<CommentResponse> content = commentPage.stream()
                .map(CommentMapper::toDto)
                .toList();

        PageResponse<CommentResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(commentPage.getNumber());
        response.setSize(commentPage.getSize());
        response.setTotalElements(commentPage.getTotalElements());
        response.setTotalPages(commentPage.getTotalPages());

        return response;
    }
}