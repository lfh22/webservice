package com.springboot.security.controller;



import com.springboot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;




/**
 * Created by umakr on 2018/4/16.
 */
@Controller
@RequestMapping("/api/system/user")
public class HomeController {
   @Autowired
    UserService userService;

    @RequestMapping(value = "/login")
    @ResponseBody
    public Object login(String username,String password){

    return userService.login( username, password);
    }

    @Cacheable(value = "Product",key ="#userId")
    @RequestMapping(value = "/ShowMenuByUserId")
    @ResponseBody
    public Object ShowMenuByUserId(Long userId){

    return userService.ShowMenuByUserId(userId);
    }


    @RequestMapping(value = "/addUser", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object addUser(String username,String password,String account,String email){

        return userService.addUser(username,password, account, email);
    }

    @RequestMapping(value = "/fetchUser", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object fetchUser(int pageNo,int pageSize){

        return userService.fetchUser(pageNo,pageSize);
    }

    @RequestMapping(value = "/fetchUserRole", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object fetchUserRole(){

        return userService.fetchUserRole();
    }

    @RequestMapping(value = "/addUserRole", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object addUserRole(Integer roleId,Integer userId){

        return userService.addUserRole(roleId,userId);
    }

}