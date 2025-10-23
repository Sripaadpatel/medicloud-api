package com.medicloud.module.user.repository;

import com.medicloud.module.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // This method is critical for UserDetailsServiceImpl
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}