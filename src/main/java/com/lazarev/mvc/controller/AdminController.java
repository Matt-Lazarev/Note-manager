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
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/api")
public class AdminController {

    private final UserService userService;

    @GetMapping("/admins")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getAllAdmins(Model model){

        List<ApplicationUser> owners = userService.getUsersByRole(UserRole.OWNER);
        List<ApplicationUser> admins = userService.getUsersByRole(UserRole.ADMIN);
        owners.addAll(admins);

        model.addAttribute("users", owners);
        return "admins/admins";
    }

    @GetMapping("/admins/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editUserForm(@PathVariable Long id, Model model){
        model.addAttribute("user", userService.getUserById(id));
        return "admins/edit_admin";
    }

    @PutMapping("/admins/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editUser(@PathVariable Long id,
                           @Valid @ModelAttribute ("user") ApplicationUser user,
                           BindingResult bindingResult,
                           Model model){

        if(bindingResult.hasErrors()){
            return "admins/edit_admin";
        }
        userService.updateUser(id, user);
        return "redirect:/api/admins";
    }

    @PutMapping("/admins/block/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String blockUser(@PathVariable Long id, Model model){
        userService.blockUser(id);
        return "redirect:/api/admins";
    }

    @PutMapping("/admins/unblock/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String unblockUser(@PathVariable Long id, Model model){
        userService.unblockUser(id);
        return "redirect:/api/admins";
    }

    @DeleteMapping("/admins/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String deleteModerator(@PathVariable Long id, Model model){
        userService.deleteUser(id);
        return "redirect:/api/admins";
    }
}

