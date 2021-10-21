package com.lazarev.mvc.controller;

import com.lazarev.mvc.entity.ApplicationUser;
import com.lazarev.mvc.entity.Note;
import com.lazarev.mvc.service.NoteService;
import com.lazarev.mvc.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@AllArgsConstructor

@Controller
@RequestMapping("/api")
public class NoteController {

    private final UserService userService;
    private final NoteService noteService;

    @GetMapping("/notes")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getAllNotes(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        ApplicationUser user = userService.getUserByLogin(userDetails.getUsername());
        model.addAttribute("notes", noteService.getAllNotesByUser(user));
        return "notes/notes";
    }

    @GetMapping("/notes/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCreateNotePage(Model model){
        model.addAttribute("note", new Note());
        return "notes/create_note";
    }


    @PostMapping("/notes/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String addNoteToUser(@Valid @ModelAttribute ("note") Note note,
                                BindingResult bindingResult, Model model,
                                Authentication authentication){

        if(bindingResult.hasErrors()){
            return "notes/create_note";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        ApplicationUser user = userService.getUserByLogin(userDetails.getUsername());

        userService.addNoteToUser(user.getId(), note);
        return "redirect:/api/notes";
    }

    @GetMapping("/notes/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String editNoteForm(@PathVariable Long id, Model model){
        model.addAttribute("note", noteService.getNoteById(id));
        return "notes/edit_note";
    }

    @PutMapping("/notes/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String editNote(@PathVariable Long id,
                           @Valid @ModelAttribute ("note") Note note,
                           BindingResult bindingResult,
                           Model model){

        if(bindingResult.hasErrors()){
            return "notes/edit_note";
        }

        noteService.updateNote(id, note);
        return "redirect:/api/notes";
    }

    @DeleteMapping("/notes/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteNote(@PathVariable Long id){
        noteService.deleteNote(id);
        return "redirect:/api/notes";
    }
}
