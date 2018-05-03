package com.springboot.security.controller;


import com.springboot.security.service.RoleService;
import com.springboot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by umakr on 2018/4/16.
 */
@Controller
@RequestMapping("/api/system/role")
public class RoleController {
   @Autowired
    UserService userService;

   @Autowired
   RoleService roleService;

    @RequestMapping(value = "/addRole", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object addRole(String role,String name){
        return roleService.addRole(role,name);
    }

    @RequestMapping(value = "/getRoles", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getRoles(String role,String name){
        return roleService.getRoles(role,name);
    }

    @RequestMapping(value = "/addResource", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object addResource(String name,String path,Integer pid,String state,String component,int type){
        return roleService.addResource(name,path,pid,state,component,type);
    }
    @RequestMapping(value = "/addRoleResource", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object addRoleResource(String roleId,String resourceId){
        return roleService.addRoleResource(roleId,resourceId);
    }

    @RequestMapping(value = "/getResource", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getResource(String name,String path,String state,String component,Integer type,Integer pageSize,Integer pageNo){
        return roleService.getResource(name,path,state,component,type,pageSize,pageNo);
    }

    @RequestMapping(value = "/getResourceOne", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getResourceOne(){
        return roleService.getResourceOne();
    }
}