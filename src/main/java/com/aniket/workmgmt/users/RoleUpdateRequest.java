package com.aniket.workmgmt.users;
public class RoleUpdateRequest {
    Long initiatorId;
    Role role;

    public RoleUpdateRequest(){

    }

    public Long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Long initiatorId) {
        this.initiatorId = initiatorId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
