package com.lazarev.mvc.controller;

import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.security.UserRole;
import com.lazarev.mvc.service.NoteService;
import com.lazarev.mvc.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
public class MainController {

    private final UserService userService;
    private final NoteService noteService;

    @GetMapping("/login")
    public String getLoginPage(){
        return "registration/login";
    }

    @GetMapping("/api/home")
    public String getHomePage(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        ApplicationUser user = userService.getUserByLogin(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if(roles.contains("ROLE_USER")){
            model.addAttribute("notes", noteService.getAllNotesByUser(user));
            return "notes/notes";
        }
        else {
            model.addAttribute("users", userService.getUsersByRole(UserRole.USER));
            return "users/users";
        }
    }

}
