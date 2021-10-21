package com.lazarev.mvc.service;

import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.entity.Note;
import com.lazarev.mvc.registration.token.ConfirmationToken;
import com.lazarev.mvc.security.UserRole;

import java.util.List;

public interface UserService {
    ApplicationUser getUserByLogin(String login);
    List<ApplicationUser> getAllUsers();
    String signUpUser(ApplicationUser applicationUser);
    ApplicationUser getUserById(Long id);
    void updateUser(Long id, ApplicationUser user);
    void deleteUser(Long id);
    void blockUser(Long id);
    void unblockUser(Long id);
    List<ApplicationUser> getUsersByRole(UserRole role);
    void addNoteToUser(Long id, Note note);
    void enableAppUser(String login);
    boolean checkIsUniqueLogin(String login);
    ApplicationUser getUserByConfirmationToken(ConfirmationToken confirmationToken);
}
