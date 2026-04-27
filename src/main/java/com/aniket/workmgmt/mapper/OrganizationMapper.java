package com.aniket.workmgmt.mapper;

import com.aniket.workmgmt.dto.OrganizationResponse;
import com.aniket.workmgmt.organizations.Organization;

public class OrganizationMapper {

    public static OrganizationResponse toDto(Organization org){
        OrganizationResponse dto = new OrganizationResponse();

        dto.setId(org.getId());
        dto.setName(org.getName());

        return dto;
    }
}