package com.aniket.workmgmt.teams;
import com.aniket.workmgmt.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {
    Page<Team> findByUsersContaining(User user, Pageable pageable);
}
