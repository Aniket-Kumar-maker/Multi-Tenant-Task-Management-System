package com.aniket.workmgmt.organizations;

import com.aniket.workmgmt.dto.OrganizationResponse;
import com.aniket.workmgmt.dto.PageResponse;
import com.aniket.workmgmt.mapper.OrganizationMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public OrganizationResponse createOrganization(@RequestBody OrganizationRequest request) {
        return OrganizationMapper.toDto(
                organizationService.addOrganization(request.getName())
        );
    }


    @GetMapping
    public PageResponse<OrganizationResponse> getOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<Organization> orgPage =
                organizationService.getAllOrganizationsPaginated(pageable);

        List<OrganizationResponse> content = orgPage.stream()
                .map(OrganizationMapper::toDto)
                .toList();

        PageResponse<OrganizationResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(orgPage.getNumber());
        response.setSize(orgPage.getSize());
        response.setTotalElements(orgPage.getTotalElements());
        response.setTotalPages(orgPage.getTotalPages());

        return response;
    }
}