package com.aniket.workmgmt.comments;

public class CommentCreateRequest {

    private String content;
    private Long userId;
    private Long taskId;

    public CommentCreateRequest(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }
}