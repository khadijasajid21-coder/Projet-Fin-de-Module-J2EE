package com.example.cantine.security;

import com.example.cantine.entity.Utilisateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String nom;
    private final String prenom;
    private final String email;
    @JsonIgnore private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean actif;

    public UserDetailsImpl(Utilisateur u) {
        this.id = u.getId();
        this.nom = u.getNom();
        this.prenom = u.getPrenom();
        this.email = u.getEmail();
        this.password = u.getPassword();
        this.actif = u.isActif();
        this.authorities = u.getRoles().stream()
            .map(r -> new SimpleGrantedAuthority(r.getNom().name()))
            .collect(Collectors.toList());
    }

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return actif; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return actif; }
}
