package com.example.outsourcingproject.user.repository;

import com.example.outsourcingproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

}
