package com.springboot.security.config;

/**
 * Created by umakr on 2018/4/16.
 */

import com.springboot.security.config.jwt.JWTUtill;
import com.springboot.security.config.jwt.JwtConfigProperties;
import com.springboot.security.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Created by yangyibo on 17/1/18.
 */
@EnableConfigurationProperties(JwtConfigProperties.class)
@Configuration
@EnableWebSecurity(debug = false)
//管控登录访问权限
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired(required = false)
    UserDetailsService customUserService;

    @Bean
    public JWTFilter jwtFilter(){
        return new JWTFilter();
    }
    @Bean
    public JwtConfigProperties jwtConfigProperties(){
        return new JwtConfigProperties();
    }


    @Bean
    public MyAuthenticationManager authenticationManagerBean() throws Exception {
        return new MyAuthenticationManager();
    }

    @Bean
    public JWTUtill jwtUtill()  {
        return new JWTUtill();
    }


    @Override
//    配置user-detail 服务。
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService). passwordEncoder(new PasswordEncoder(){
            @Override
            public String encode(CharSequence arg0) {
                return arg0.toString();
            }

            @Override
            public boolean matches(CharSequence arg0, String arg1) {
                return arg1.equals(arg0.toString());
            }
        });//在此处应用自定义PasswordEncoder;
    }
    @Override
//    通过重载，配置如何通过拦截器保护请求。
    protected void configure(HttpSecurity http) throws Exception {
// 关闭csrf验证
        http.csrf().disable()
                // 对请求进行认证
                .authorizeRequests()
                // 所有 / 的所有请求 都放行
                .antMatchers("/api/system/user/login").permitAll()
                // 所有 /login 的POST请求 都放行
//                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // 权限检查
//                .antMatchers("/hello").hasAuthority("AUTH_WRITE")
                // 角色检查
//                .antMatchers("/world").hasRole("ADMIN")
                // 所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                // 添加一个过滤器验证其他请求的Token是否合法
//                .addFilterBefore(new JWTFilter(),UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(),UsernamePasswordAuthenticationFilter.class)
                .logout().
                permitAll() //注销行为任意访问
                .invalidateHttpSession(true) ; // 指定是否在注销时让HttpSession无效。默认设置为true


//以下这句就可以控制单个用户只能创建一个session，也就只能在服务器登录一次
        http.sessionManagement().maximumSessions(1).expiredUrl("/login");

    }
    @Bean
    MyAccessDecisionManager securityAccessDecisionManager(){

        return new MyAccessDecisionManager();
    }




}
