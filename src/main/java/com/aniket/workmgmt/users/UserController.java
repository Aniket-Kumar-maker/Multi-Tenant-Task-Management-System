package com.aniket.workmgmt.users;

import com.aniket.workmgmt.dto.PageResponse;
import com.aniket.workmgmt.dto.UserResponse;
import com.aniket.workmgmt.dto.TeamResponse;
import com.aniket.workmgmt.mapper.UserMapper;
import com.aniket.workmgmt.mapper.TeamMapper;
import com.aniket.workmgmt.teams.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        return UserMapper.toDto(userService.createUser(request.getUserName(), request.getUserEmail(), request.getOrgId()));
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id){
        return UserMapper.toDto(userService.getUser(id));
    }

    @GetMapping
    public PageResponse<UserResponse> getUsers(
            @RequestParam Long initiatorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<User> userPage =
                userService.getUsersPaginated(initiatorId, pageable);

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

    @GetMapping("/{id}/teams")
    public PageResponse<TeamResponse> getTeamsOfUser(
            @PathVariable Long initiatorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<Team> teamPage =
                userService.fetchTeamsPaginated(initiatorId, pageable);

        List<TeamResponse> content = teamPage.stream()
                .map(TeamMapper::toDto)
                .toList();

        PageResponse<TeamResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(teamPage.getNumber());
        response.setSize(teamPage.getSize());
        response.setTotalElements(teamPage.getTotalElements());
        response.setTotalPages(teamPage.getTotalPages());

        return response;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId,
                             @RequestParam Long initiatorId){
        userService.deleteUser(initiatorId, userId);
        return "User deleted successfully";
    }


    @PatchMapping("/{userId}/role")
    public UserResponse updateRole(@PathVariable Long userId,
                                   @RequestBody RoleUpdateRequest request){
        return UserMapper.toDto(
                userService.updateRole(
                        userId,
                        request.getInitiatorId(),
                        request.getRole()
                )
        );
    }
}