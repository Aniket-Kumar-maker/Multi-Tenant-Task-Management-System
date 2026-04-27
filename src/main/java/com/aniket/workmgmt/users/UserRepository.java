package com.aniket.workmgmt.users;
import com.aniket.workmgmt.teams.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByOrganizationId(Long orgId);
    List<User> findByOrganizationIdAndDeletedFalse(Long orgId);
    Page<User> findByOrganizationIdAndDeletedFalse(Long orgId, Pageable pageable);
    Page<User> findByTeamsContainingAndDeletedFalse(Team team, Pageable pageable);
}
