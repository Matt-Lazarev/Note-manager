package com.lazarev.mvc.registration;

import com.lazarev.mvc.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class RegistrationRequest {

    @Size(min=2, max=20, message = "Firstname should be between 2 and 20 symbols")
    @Pattern(regexp = "^[A-Z][A-Za-z]+$", message = "Firstname should start from the capital letter")
    private String firstname;

    @Size(min=2, max=20, message = "Lastname should be between 2 and 20 symbols")
    @Pattern(regexp = "^[A-Z][A-Za-z]+$", message = "Lastname should start from the capital letter")
    private String lastname;

    @Size(min=2, max=20, message = "Login should be between 2 and 20 symbols")
    private String login;

    @NotNull(message = "Password should not be null")
    @NotEmpty(message = "Password should not be empty")
    @Size(min=2, max=20, message = "Password should be between 2 and 20 symbols")
    private String password;

    private UserRole role;
}
