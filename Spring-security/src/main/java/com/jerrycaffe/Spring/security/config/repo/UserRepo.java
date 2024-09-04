package com.jerrycaffe.Spring.security.config.repo;

import com.jerrycaffe.Spring.security.config.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
}
