package com.boubyan.studentmanagement.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.boubyan.studentmanagement.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.password = password;
    }

    public static UserDetailsImpl build(User user) {
        var authorities = getGrantedAuthorities(user);
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }

    public static ArrayList<GrantedAuthority> getGrantedAuthorities(User user) {
        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(user.getUserType()
                .name()));
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
