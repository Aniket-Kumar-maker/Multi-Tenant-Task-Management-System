package com.aniket.workmgmt.users;
import com.aniket.workmgmt.activitylogs.ActivityLogService;
import com.aniket.workmgmt.organizations.Organization;
import com.aniket.workmgmt.organizations.OrganizationRepository;
import com.aniket.workmgmt.teams.Team;
import com.aniket.workmgmt.teams.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final ActivityLogService activityLogService;
    private final TeamRepository teamRepository;

    public UserService(UserRepository uRep, OrganizationRepository oRep, ActivityLogService asL, TeamRepository tRep){
        this.userRepository = uRep;
        this.organizationRepository = oRep;
        this.activityLogService = asL;
        this.teamRepository = tRep;
    }

    public User createUser(String name, String email, Long org_id) {
        Organization org = organizationRepository.findById(org_id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setOrganization(org);

        if (!userRepository.existsByOrganizationId(org_id))
            user.setRole(Role.ADMIN);
        else
            user.setRole(Role.CONTRIBUTOR);

        return userRepository.save(user);
    }

    public User getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getUsers(Long initiatorId){
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return userRepository.findByOrganizationIdAndDeletedFalse(
                initiator.getOrganization().getId()
        );
    }

    public Page<User> getUsersPaginated(Long initiatorId, Pageable pageable){
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return userRepository.findByOrganizationIdAndDeletedFalse(
                initiator.getOrganization().getId(),
                pageable
        );
    }

    public List<Team> fetchTeams(Long user_id){
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return user.getTeams();
    }

    public Page<Team> fetchTeamsPaginated(Long userId, Pageable pageable){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return teamRepository.findByUsersContaining(user, pageable);
    }

    public void deleteUser(Long initiatorId, Long userId){

        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new RuntimeException("Initiator not found!"));

        User toDelete = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (toDelete.isDeleted()) {
            throw new RuntimeException("User is already deleted");
        }

        if (initiator.getRole() == Role.CONTRIBUTOR) {
            throw new RuntimeException("Deletion not allowed!");
        }

        if (!initiator.getOrganization().getId()
                .equals(toDelete.getOrganization().getId())) {
            throw new RuntimeException("Cannot delete user outside of org!");
        }

        for (Team team : toDelete.getTeams()) {
            team.getUsers().remove(toDelete);
        }
        toDelete.setDeleted(true);
        String logMessage = String.format(
                "%s deleted user %s",
                initiator.getName(),
                toDelete.getName()
        );
        activityLogService.addLog(logMessage,initiator,null);
        userRepository.save(toDelete);
    }

    public User updateRole(Long userId, Long initiatorId, Role role){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        User initiator = userRepository.findById(initiatorId).orElseThrow(() -> new RuntimeException("User not found!"));

        if(initiator.getRole() == Role.MANAGER || initiator.getRole() == Role.CONTRIBUTOR)
            throw new RuntimeException("Contributors and Managers cannot update roles!");

        if(!initiator.getOrganization().getId().equals(user.getOrganization().getId()))
            throw new RuntimeException("Cannot update roles of users outside of org!");

        Role oldRole = user.getRole();
        user.setRole(role);
        String logMessage = String.format(
                "%s updated role of %s from '%s' to '%s'",
                initiator.getName(),
                user.getName(),
                oldRole,
                user.getRole()
        );
        activityLogService.addLog(logMessage,initiator,null);
        return userRepository.save(user);
    }
}