package com.aniket.workmgmt.teams;
import com.aniket.workmgmt.dto.PageResponse;
import com.aniket.workmgmt.dto.UserResponse;
import com.aniket.workmgmt.mapper.UserMapper;
import com.aniket.workmgmt.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    @PostMapping
    public Team createTeam(@RequestBody TeamRequest request){ //create team and assign org
        return teamService.addTeam(request.getTeamName(), request.getOrgId());
    }

    @GetMapping("/{id}")
    public Team getTeamInfo(@PathVariable Long id){
        return teamService.getTeamInfo(id);
    }


    @PostMapping("/{teamId}/users/{userId}")
    public void addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId){
        teamService.addUserToTeam(teamId, userId);
    }

    @GetMapping("/{teamId}/users")
    public PageResponse<UserResponse> fetchUsers(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<User> userPage =
                teamService.fetchUsersPaginated(teamId, pageable);

        List<UserResponse> content = userPage.stream()
                .map(UserMapper::toDto)
                .toList();

        PageResponse<UserResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(userPage.getNumber());
        response.setSize(userPage.getSize());
        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());

        return response;
    }

    @DeleteMapping("/{teamId}")
    public String deleteTeam(@PathVariable Long teamId, @RequestParam Long userId){
        teamService.deleteTeam(teamId,userId);
        return "Team deleted successfully!";
    }

}
