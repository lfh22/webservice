package com.springboot.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by umakr on 2018/4/20.
 */
public class MyAuthenticationManager implements AuthenticationManager {
    static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

    static {

        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));

    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getName().equals(authentication.getCredentials())) {

            return new UsernamePasswordAuthenticationToken(authentication.getName(),

                    authentication.getCredentials(),AUTHORITIES);

        }

        throw new BadCredentialsException("Bad Credentials");
    }
}
