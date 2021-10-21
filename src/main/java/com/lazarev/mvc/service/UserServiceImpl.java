package com.lazarev.mvc.service;

import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.entity.Note;
import com.lazarev.mvc.registration.token.ConfirmationToken;
import com.lazarev.mvc.registration.token.ConfirmationTokenRepository;
import com.lazarev.mvc.repository.UserRepository;
import com.lazarev.mvc.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository tokenRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<ApplicationUser> userOptional = userRepository.getUserByLogin(login);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot load user with login '%s'", login));
            log.error("Cannot find user by login", ex);
            throw ex;
        }
    }

    @Override
    public ApplicationUser getUserByLogin(String login) {
        Optional<ApplicationUser> userOptional = userRepository.getUserByLogin(login);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with login '%s'", login));
            log.error("Cannot find user by login", ex);
            throw ex;
        }
    }

    @Override
    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public String signUpUser(ApplicationUser user){
        Optional<ApplicationUser> userOptional = userRepository.getUserByLogin(user.getLogin());

        if(userOptional.isEmpty()){
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15));
            ConfirmationToken savedToken =  tokenRepository.save(confirmationToken);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setConfirmationToken(savedToken);
            userRepository.save(user);

            return token;
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with login '%s'", user.getLogin()));
            log.error("Cannot save user", ex);
            throw ex;
        }
    }

    @Override
    public ApplicationUser getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void updateUser(Long id, ApplicationUser user) {
        Optional<ApplicationUser> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            ApplicationUser retrievedUser = optionalUser.get();
            retrievedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            retrievedUser.setFirstname(user.getFirstname());
            retrievedUser.setLastname(user.getLastname());
            retrievedUser.setRole(user.getRole());

            userRepository.save(retrievedUser);
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with id '%d'", user.getId()));
            log.error("Cannot find user", ex);
            throw ex;
        }
    }

    @Override
    public void deleteUser(Long id) {
        Optional<ApplicationUser> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            userRepository.delete(optionalUser.get());
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with id '%d'", id));
            log.error("Cannot delete user", ex);
            throw ex;
        }

    }

    @Override
    public void blockUser(Long id) {
        Optional<ApplicationUser> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            ApplicationUser retrievedUser = optionalUser.get();
            retrievedUser.setAccountBlocked(true);
            userRepository.save(retrievedUser);
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with id '%d'", id));
            log.error("Cannot block user", ex);
            throw ex;
        }
    }

    @Override
    public void unblockUser(Long id) {
        Optional<ApplicationUser> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            ApplicationUser retrievedUser = optionalUser.get();
            retrievedUser.setAccountBlocked(false);
            userRepository.save(retrievedUser);
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with id '%d'", id));
            log.error("Cannot unblock user", ex);
            throw ex;
        }
    }

    @Override
    public List<ApplicationUser> getUsersByRole(UserRole role) {
        return userRepository.findByRoleIs(role);
    }

    @Override
    public void addNoteToUser(Long id, Note note) {
        Optional<ApplicationUser> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            ApplicationUser retrievedUser = optionalUser.get();
            note.setCreationDateTime(LocalDateTime.now());
            retrievedUser.getNotes().add(note);
            userRepository.save(retrievedUser);
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with id '%d'", id));
            log.error("Cannot add note to user", ex);
            throw ex;
        }
    }

    @Override
    public void enableAppUser(String login) {
        Optional<ApplicationUser> optionalUser = userRepository.getUserByLogin(login);
        if(optionalUser.isPresent()){
            ApplicationUser retrievedUser = optionalUser.get();
            retrievedUser.setAccountEnabled(true);
            userRepository.save(retrievedUser);
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with login '%s'", login));
            log.error("Cannot enable user", ex);
            throw ex;
        }
    }

    @Override
    public boolean checkIsUniqueLogin(String login) {
        return userRepository.getUserByLogin(login).isPresent();
    }

    @Override
    public ApplicationUser getUserByConfirmationToken(ConfirmationToken confirmationToken) {
        Optional<ApplicationUser> userOptional= userRepository.findUserByConfirmationToken(confirmationToken);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        else {
            RuntimeException ex = new IllegalStateException(
                    String.format("Cannot find user with token '%s'", confirmationToken.getToken()));
            log.error("Cannot find user by token", ex);
            throw ex;
        }
    }
}
