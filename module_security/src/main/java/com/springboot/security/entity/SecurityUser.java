package com.springboot.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by umakr on 2018/4/20.
 */
public class SecurityUser extends SysUser implements UserDetails {

    public SecurityUser(SysUser sUser){
        if(sUser != null){
            this.setId(sUser.getId());
            this.setUsername(sUser.getUsername());
            this.setPassword(sUser.getPassword());
            this.setRoles(sUser.getRoles());
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        List<SysRole> roles = this.getRoles();
            for (SysRole role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            }
        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
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
}
