package com.springboot.security.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.springboot.security.config.jwt.JWTUtill;
import com.springboot.security.dao.UserDaoMapper;
import com.springboot.security.entity.SecurityUser;
import com.springboot.security.entity.SysUser;
import com.springboot.security.util.JSONResultUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by umakr on 2018/4/20.
 */
@Service
public class UserService extends ServiceImpl<UserDaoMapper, SysUser> {
    @Autowired
    CustomUserService customUserService;

    @Autowired
    MyAuthenticationManager authenticationManager;

    @Autowired
    JWTUtill jwtUtill;

    @Autowired
    UserDaoMapper userDaoMapper;

    @Autowired
    RedisTemplate redisTemplate;

    public Object login(String username,String password){
        try{

            //用户名、密码组合生成一个Authentication对象（也就是UsernamePasswordAuthenticationToken对象）
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);

            // 生成的这个token对象会传递给一个AuthenticationManager对象用于验证。
            // 当成功认证后，AuthenticationManager返回一个Authentication对象。
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = customUserService.loadUserByUsername(username);

            final String token = jwtUtill.generateToken(userDetails);

            Long userId = ((SecurityUser)(userDetails)).getId();
            Map<String,Object> dataMap = Maps.newHashMap();

            dataMap.put("token",token);
            dataMap.put("userId", userId);

            Map<String,Object> userDetail = userDaoMapper.getUserDetailsByUserId(userId);
            if(userDetails != null) {
                dataMap.putAll(userDetail);
            }
            dataMap.put("token",token);
            return JSONResultUtil.fillResultString(0, "用户登录成功", dataMap);
        }catch(BadCredentialsException e){
            return JSONResultUtil.fillResultString(1, "获取用户信息错误", JSONObject.NULL);
        }

    }

    public Object addUser(String username, String password,String account,String email) {
        if(userDaoMapper.addUser(username,password, account, email)){
            return JSONResultUtil.fillResultString(0, "操作成功", JSONObject.NULL);
        }else{
            return JSONResultUtil.fillResultString(1, "操作失败", JSONObject.NULL);
        }
    }



    public Object fetchUser(int pageNo, int pageSize) {
        Map<String,Object> dataMap = Maps.newHashMap();

        dataMap.put("list",userDaoMapper.fetchUser(pageNo,pageSize));

        Map<String,Object> paginationMap = Maps.newHashMap();
        paginationMap.put("pageNo",pageNo);
        paginationMap.put("pageSize", pageSize);

        dataMap.put("pagination", paginationMap);
        return JSONResultUtil.fillResultString(0, "操作成功", dataMap);
    }

    public Object fetchUserRole() {

        return JSONResultUtil.fillResultString(0, "操作成功", userDaoMapper.fetchUserRole());
    }

    public Object addUserRole(int roleId, int userId) {

        if(userDaoMapper.getUserRole(roleId,userId)==null){
            if(userDaoMapper.addUserRole(roleId,userId)){
                return JSONResultUtil.fillResultString(0, "操作成功", JSONObject.NULL);
            }else{
                return JSONResultUtil.fillResultString(1, "操作失败", JSONObject.NULL);
            }
        }else{
            return JSONResultUtil.fillResultString(1, "操作失败：用户已拥有此权限", JSONObject.NULL);
        }
    }

    public Object ShowMenuByUserId(Long userId) {
        System.out.println("--------进入菜单查询方法--------");
        return JSONResultUtil.fillResultString(0, "", userDaoMapper.ShowMenuByUserId(userId));

    }
}
