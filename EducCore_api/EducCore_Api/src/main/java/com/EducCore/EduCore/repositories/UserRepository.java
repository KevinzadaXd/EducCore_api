package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Usado pelo Spring Security (AuthorizationService)
    UserDetails findByLogin(String login);

    // Usado pelo UserController para verificar duplicidade de login sem cast
    User findUserByLogin(String login);
}