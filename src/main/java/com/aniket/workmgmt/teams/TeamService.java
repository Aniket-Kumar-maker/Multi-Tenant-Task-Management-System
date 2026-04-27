package com.aniket.workmgmt.teams;
import com.aniket.workmgmt.activitylogs.ActivityLogService;
import com.aniket.workmgmt.organizations.Organization;
import com.aniket.workmgmt.organizations.OrganizationRepository;
import com.aniket.workmgmt.users.Role;
import com.aniket.workmgmt.users.User;
import com.aniket.workmgmt.users.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public TeamService(TeamRepository tR, OrganizationRepository oR, UserRepository uR, ActivityLogService asL){
        teamRepository = tR;
        organizationRepository = oR;
        userRepository = uR;
        activityLogService = asL;
    }

    public Team addTeam(String name, Long orgId){ //creating team and assigning org
        Organization org = organizationRepository.findById(orgId).orElseThrow(() -> new RuntimeException("Organization not found"));

        Team team = new Team();
        team.setName(name);
        team.setOrganization(org);
        return teamRepository.save(team);
    }

    public Team getTeamInfo(Long teamId){ //get info corresponding to team id
        return teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
    }

    public void addUserToTeam(Long team_id, Long user_id){
        Team team = teamRepository.findById(team_id).orElseThrow(() -> new RuntimeException("Team Not Found!"));
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User Not Found!"));

        if(!Objects.equals(user.getOrganization().getId(), team.getOrganization().getId()))
            throw new RuntimeException("Organization and Teams are different!");

        if (!user.getTeams().contains(team)) {
            user.getTeams().add(team);
            team.getUsers().add(user); //sync
            userRepository.save(user);
            return;
        }

        throw new RuntimeException("Duplicate entries not allowed!");
    }

    public List<User> fetchUsers(Long team_id){
        Team team = teamRepository.findById(team_id).orElseThrow(() -> new RuntimeException("Team Not Found!"));
        return team.getUsers();
    }

    public Page<User> fetchUsersPaginated(Long teamId, Pageable pageable){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team Not Found!"));

        return userRepository.findByTeamsContainingAndDeletedFalse(team, pageable);
    }

    public void deleteTeam(Long userId, Long teamId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found!"));
        if(team.isDeleted())
            throw new RuntimeException("Team is already deleted!");

        if(user.getRole() == Role.CONTRIBUTOR){
            throw new RuntimeException("Contributor cannot delete teams!");
        }

        if(!user.getOrganization().getId().equals(team.getOrganization().getId())){
            throw new RuntimeException("Cannot delete team outside of org!");
        }

        for(User u : team.getUsers()){
            u.getTeams().remove(team);
        }
        team.setDeleted(true);
        String logMessage = String.format(
                "%s deleted team %s",
                user.getName(),
                team.getName()
        );
        activityLogService.addLog(logMessage,user,null);
        teamRepository.save(team);
    }

}
