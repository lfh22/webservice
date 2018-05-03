package com.springboot.security.service;

/**
 * Created by umakr on 2018/4/16.
 */

import com.springboot.security.dao.UserDaoMapper;
import com.springboot.security.entity.SecurityUser;
import com.springboot.security.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Created by yangyibo on 17/1/18.
 */
@Service
//将用户权限交给 springsecurity 进行管控
public class CustomUserService implements UserDetailsService { //自定义UserDetailsService 接口

    @Autowired
    UserDaoMapper userDao;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) { //重写loadUserByUsername 方法获得 userdetails 类型用户
        SysUser user =  userDao.findByUserName(username);

        if(user != null){
            return new SecurityUser(user);
        }else{
            throw new UsernameNotFoundException("用户名不存在。");
        }
    }
}
