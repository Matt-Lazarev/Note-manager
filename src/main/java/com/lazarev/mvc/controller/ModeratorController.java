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
public class ModeratorController {

    private final UserService userService;

    @GetMapping("/moderators")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String getAllModerators(Model model){
        model.addAttribute("users", userService.getUsersByRole(UserRole.MODERATOR));
        return "moderators/moderators";
    }

    @GetMapping("/moderators/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editUserForm(@PathVariable Long id, Model model){
        model.addAttribute("user", userService.getUserById(id));
        return "moderators/edit_moderator";
    }

    @PutMapping("/moderators/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editUser(@PathVariable Long id,
                           @Valid @ModelAttribute ("user") ApplicationUser user,
                           BindingResult bindingResult,
                           Model model){

        if(bindingResult.hasErrors()){
            return "moderators/edit_moderator";
        }
        userService.updateUser(id, user);
        return "redirect:/api/moderators";
    }

    @PutMapping("/moderators/block/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String blockUser(@PathVariable Long id, Model model){
        userService.blockUser(id);
        return "redirect:/api/moderators";
    }

    @PutMapping("/moderators/unblock/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String unblockUser(@PathVariable Long id, Model model){
        userService.unblockUser(id);
        return "redirect:/api/moderators";
    }

    @DeleteMapping("/moderators/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_OWNER','ROLE_ADMIN')")
    public String deleteModerator(@PathVariable Long id, Model model){
        userService.deleteUser(id);
        return "redirect:/api/moderators";
    }
}
