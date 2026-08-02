package com.aniket.workmgmt.users;
import com.aniket.workmgmt.organizations.Organization;

public class UserRequest {

    private String userName;
    private String userEmail;
    private Long orgId;
    private String passWord;

    public UserRequest(String name, String email, Long id, String passWord){
        userName = name;
        userEmail = email;
        orgId = id;
        this.passWord = passWord;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
