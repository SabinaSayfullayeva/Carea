package com.example.carea.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    Boolean enabled = true;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return List.of(this.role);
    }

    @Override
    public String getUsername()
    {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return this.enabled;
    }

    @Override
    public boolean isEnabled()
    {
        return this.enabled;
    }
}

