package com.lazarev.mvc.controller;

import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.security.UserRole;
import com.lazarev.mvc.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getAllUsers(Model model){
        model.addAttribute("users", userService.getUsersByRole(UserRole.USER));
        return "users/users";
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getUserById(@PathVariable Long id, Model model){
        model.addAttribute("user", userService.getUserById(id));
        return "users/show_user";
    }

    @GetMapping("/users/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editUserForm(@PathVariable Long id, Model model){
        model.addAttribute("user", userService.getUserById(id));
        return "users/edit_user";
    }

    @PutMapping("/users/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editUser(@PathVariable Long id,
                           @Valid @ModelAttribute ("user") ApplicationUser user,
                           BindingResult bindingResult,
                           Model model){

        if(bindingResult.hasErrors()){
            return "users/edit_user";
        }

        userService.updateUser(id, user);
        return "redirect:/api/home";
    }

    @DeleteMapping("/users/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "redirect:/api/home";
    }

    @PutMapping("/users/block/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String blockUser(@PathVariable Long id, Model model){
        userService.blockUser(id);
        return "redirect:/api/home";
    }

    @PutMapping("/users/unblock/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String unblockUser(@PathVariable Long id, Model model){
        userService.unblockUser(id);
        return "redirect:/api/home";
    }
}
