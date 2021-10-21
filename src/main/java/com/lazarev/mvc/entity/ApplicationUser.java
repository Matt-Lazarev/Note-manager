package com.lazarev.mvc.entity;

import com.lazarev.mvc.registration.token.ConfirmationToken;
import com.lazarev.mvc.security.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor

@Entity
@Table(name = "users", uniqueConstraints =
        @UniqueConstraint(name = "unique_login_idx", columnNames = "login"))
public class ApplicationUser implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min=2, max=20, message = "Firstname should be between 2 and 20 symbols")
    @Pattern(regexp = "^[A-Z][A-Za-z]+$", message = "Firstname should start from the capital letter")
    @Column(name = "firstname")
    private String firstname;

    @Size(min=2, max=20, message = "Lastname should be between 2 and 20 symbols")
    @Pattern(regexp = "^[A-Z][A-Za-z]+$", message = "Lastname should start from the capital letter")
    @Column(name = "lastname")
    private String lastname;

    @Size(min=2, max=20, message = "Login should be between 2 and 20 symbols")
    @Column(name = "login")
    private String login;

    @NotNull(message = "Password should not be null")
    @NotEmpty(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "blocked", nullable = false)
    private boolean isAccountBlocked = false;

    @Column(name = "enabled", nullable = false)
    private boolean isAccountEnabled = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Note> notes;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            nullable = false,
            name = "token_id"
    )
    private ConfirmationToken confirmationToken;

    public ApplicationUser(String firstname, String lastname, String login, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAccountEnabled;
    }
}
