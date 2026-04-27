package com.aniket.workmgmt.mapper;

import com.aniket.workmgmt.comments.Comment;
import com.aniket.workmgmt.dto.CommentResponse;

public class CommentMapper {

    public static CommentResponse toDto(Comment comment){
        CommentResponse dto = new CommentResponse();

        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserName(comment.getUser().getName());
        dto.setTaskId(comment.getTask().getId());
        dto.setCreatedAt(comment.getCreatedAt());

        return dto;
    }
}