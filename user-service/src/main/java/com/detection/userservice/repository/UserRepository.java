package com.detection.userservice.repository;

import com.detection.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserNameAndPassword(String userName, String password);
}
