package com.lazarev.mvc.repository;

import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.registration.token.ConfirmationToken;
import com.lazarev.mvc.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> getUserByLogin(String login);
    List<ApplicationUser> findByRoleIs(UserRole role);
    Optional<ApplicationUser> findUserByConfirmationToken(ConfirmationToken token);
}
