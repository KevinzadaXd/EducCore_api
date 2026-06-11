package com.EducCore.EduCore.domain.User;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "\"users\"")
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String login;

    private String telephone;

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private UserRole role;

    @Column(name = "last_access")
    private LocalDateTime lastAccess;

    // ✅ CORRIGIDO: joinColumn = id_user, column = id_group (bate com o banco)
    @ElementCollection
    @CollectionTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "id_user")
    )
    @Column(name = "id_group")
    private List<Long> gruposIds = new ArrayList<>();

    // ✅ CORRIGIDO: joinColumn = id_user, column = id_course (bate com o banco)
    @ElementCollection
    @CollectionTable(
            name = "course_user",
            joinColumns = @JoinColumn(name = "id_user")
    )
    @Column(name = "id_course")
    private List<Long> cursosIds = new ArrayList<>();

    public User(String name, String login, String telephone, LocalDate birthDate, String password, UserRole role) {
        this.name = name;
        this.login = login;
        this.telephone = telephone;
        this.birthDate = birthDate;
        this.password = password;
        this.role = role;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return login;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() { return true; }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() { return true; }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    @JsonIgnore
    public boolean isEnabled() { return true; }
}