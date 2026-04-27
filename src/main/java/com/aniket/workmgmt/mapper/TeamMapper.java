package com.aniket.workmgmt.mapper;
import com.aniket.workmgmt.dto.TeamResponse;
import com.aniket.workmgmt.teams.Team;

public class TeamMapper {

    public static TeamResponse toDto(Team team){
        TeamResponse dto = new TeamResponse();

        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setOrganizationName(team.getOrganization().getName());

        return dto;
    }
}