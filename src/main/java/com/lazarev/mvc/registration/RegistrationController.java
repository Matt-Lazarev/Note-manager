package com.lazarev.mvc.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("request", new RegistrationRequest());
        return "registration/register_form";
    }

    @PostMapping("/register")
    public String register( @ModelAttribute("request") @Valid RegistrationRequest request,
                           BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "registration/register_form";
        }

        if(registrationService.checkIsUniqueLogin(request.getLogin())){
            model.addAttribute("not_unique_login", "true");
            return "registration/register_form";
        }

        registrationService.register(request);

        return  "redirect:/login";
    }

    @GetMapping( "/confirm")
    public String confirm(@RequestParam("token") String token) {
        if(registrationService.confirmToken(token)){
            return "redirect:/login";
        }
        else {
            return "registration/error_message";
        }

    }
}
