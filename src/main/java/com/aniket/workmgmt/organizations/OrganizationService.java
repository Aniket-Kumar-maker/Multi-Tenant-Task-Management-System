package com.aniket.workmgmt.organizations;
import com.aniket.workmgmt.users.User;
import com.aniket.workmgmt.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository orgRepository;

    public Organization addOrganization(String orgName) {

        Organization org = new Organization();
        org.setName(orgName);
        return orgRepository.save(org);
    }

    public Page<Organization> getAllOrganizationsPaginated(Pageable pageable) {
        return orgRepository.findAll(pageable);
    }

    public List<Organization> getAllOrganizations() {
        return orgRepository.findAll();
    }
}
