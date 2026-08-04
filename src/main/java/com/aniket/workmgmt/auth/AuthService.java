package com.aniket.workmgmt.auth;

import com.aniket.workmgmt.organizations.Organization;
import com.aniket.workmgmt.organizations.OrganizationService;
import com.aniket.workmgmt.organizations.OrganizationRepository;
import com.aniket.workmgmt.security.CustomUserDetails;
import com.aniket.workmgmt.security.JwtService;
import com.aniket.workmgmt.users.Role;
import com.aniket.workmgmt.users.User;
import com.aniket.workmgmt.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {

    private final OrganizationService organizationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OrganizationRepository orgRep;

    public AuthService(OrganizationService organizationService, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService, OrganizationRepository orgRep) {
        this.organizationService = organizationService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.orgRep = orgRep;
    }

    public LoginResponse register(RegisterRequest request) {
        if (orgRep.existsByName(request.getOrganizationName())) {
            throw new RuntimeException("Organization already exists.");
        }

        if(userRepository.findByEmail(request.getAdminEmail()).isPresent()){
            throw new RuntimeException("Email already exists.");
        }

        Organization organization = organizationService.addOrganization(request.getOrganizationName());
        User user = new User();
        user.setName(request.getAdminName());
        user.setEmail(request.getAdminEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOrganization(organization);
        user.setRole(Role.ADMIN);

        User savedUser = userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }
}
