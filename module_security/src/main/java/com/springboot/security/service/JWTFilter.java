package com.springboot.security.service;

import com.google.common.collect.Maps;
import com.springboot.security.config.jwt.JWTUtill;
import com.springboot.security.config.jwt.JwtConfigProperties;
import com.springboot.security.util.SecurityStatusException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by umakr on 2018/4/21.
 */
public class JWTFilter  extends OncePerRequestFilter{
    @Autowired
    private CustomUserService customUserService;

    @Autowired
    JwtConfigProperties jwtConfigProperties;

    @Autowired
    JWTUtill jwtUtill;

//    private CustomUserService customUserService;
//    private JwtConfigProperties jwtConfigProperties;
//    private JWTUtill jwtUtill;
//
//    @Autowired
//    public JWTFilter(CustomUserService customUserService,JwtConfigProperties jwtConfigProperties,JWTUtill jwtUtill){
//        this.customUserService = customUserService;
//        this.jwtConfigProperties = jwtConfigProperties;
//        this.jwtUtill = jwtUtill;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isLogin = request.getRequestURI().contains("/api/system/user/login");

        String authHeader = request.getHeader("Authorization");
        if(authHeader != null &&authHeader.startsWith(jwtConfigProperties.getHeadToken())&&!isLogin){

            final String authToken = authHeader.substring(jwtConfigProperties.getHeadToken().length());
            String key = jwtConfigProperties.getSecret();

            try{
                Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
                String subject = jws.getBody().getSubject();

                if(subject !=null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = this.customUserService.loadUserByUsername(subject);
                    Map<String,Object> map = Maps.newHashMap();
                    if(jwtUtill.validateToken(authToken, userDetails,map,request.getRequestURI())){
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                    if(!map.isEmpty()){
                        if(map.get("expired") != null){
                            throw new BadCredentialsException("token expired");
                        }
                        if(map.get("pdExpired") != null){
                            throw new BadCredentialsException("password has resetted");
                        }
                    }
                }
            }catch (ExpiredJwtException eje){ //超时。
                response.getWriter().write(SecurityStatusException.tokenExpired());
                return;
            }catch (Exception e){
                throw new BadCredentialsException(e.getLocalizedMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
