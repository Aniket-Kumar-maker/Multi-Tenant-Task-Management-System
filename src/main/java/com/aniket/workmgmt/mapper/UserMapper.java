package com.aniket.workmgmt.mapper;

import com.aniket.workmgmt.dto.UserResponse;
import com.aniket.workmgmt.users.User;

public class UserMapper {

    public static UserResponse toDto(User user){
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setOrganizationName(user.getOrganization().getName());
        return dto;
    }
}