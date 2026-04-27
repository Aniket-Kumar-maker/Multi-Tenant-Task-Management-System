package com.aniket.workmgmt.users;
import com.aniket.workmgmt.organizations.Organization;

public class UserRequest {

    private String userName;
    private String userEmail;
    private Long orgId;

    public UserRequest(String name, String email, Long id){
        userName = name;
        userEmail = email;
        orgId = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long org) {
        this.orgId = org;
    }
}
