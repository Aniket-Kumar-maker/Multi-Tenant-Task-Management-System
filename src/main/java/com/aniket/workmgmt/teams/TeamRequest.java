package com.aniket.workmgmt.teams;

public class TeamRequest {
    private String teamName;
    private Long orgId;

    public TeamRequest(String name, Long id){
        teamName = name;
        orgId = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
