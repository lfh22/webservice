package com.springboot.security.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.springboot.security.dao.ResourceMapper;
import com.springboot.security.dao.RoleDaoMapper;
import com.springboot.security.entity.Resource;
import com.springboot.security.entity.SysRole;
import com.springboot.security.util.JSONResultUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by umakr on 2018/4/24.
 */
@Service
public class RoleService extends ServiceImpl<ResourceMapper, Resource> {
    @Autowired
    RoleDaoMapper roleDaoMapper;

    @Autowired
    ResourceService resourceService;

    public Object addRole(String role, String name) {

        if(roleDaoMapper.addRole(role,name)){
            return JSONResultUtil.fillResultString(0, "操作成功", JSONObject.NULL);
        }else{
            return JSONResultUtil.fillResultString(1, "操作失败", JSONObject.NULL);
        }
    }

    public Object getRoles(String pageNo, String pageSize) {
        Map<String,Object> dataMap = Maps.newHashMap();

        dataMap.put("list",roleDaoMapper.getRoles(pageNo,pageSize));

        Map<String,Object> paginationMap = Maps.newHashMap();
        paginationMap.put("pageNo",pageNo);
        paginationMap.put("pageSize", pageSize);

        dataMap.put("pagination", paginationMap);
        return JSONResultUtil.fillResultString(0, "操作成功", dataMap);
    }

    public Object addResource(String name, String path,Integer pid, String state, String component,int type) {

        Resource resource = new Resource();
        resource.setName(name);
        resource.setType(type);
        resource.setPath(path);
        if(pid != null) resource.setPid(pid);
        if(state != null) resource.setState(state);
        if(component != null) resource.setComponent(component);

        if(resourceService.insert(resource)){
            return JSONResultUtil.fillResultString(0, "操作成功", JSONObject.NULL);
        }else{
            return JSONResultUtil.fillResultString(1, "操作失败", JSONObject.NULL);
        }
    }

    public Object getResource(String name, String path, String state, String component, Integer type, Integer pageSize, Integer pageNo) {

        EntityWrapper<Resource> entityWrapper = new EntityWrapper<Resource>();
        if(name != null) entityWrapper.eq("name",name);
        if(path != null) entityWrapper.eq("path",path);
        if(state != null) entityWrapper.eq("state",state);
        if(component != null) entityWrapper.eq("component",component);
        if(type!= null) entityWrapper.ge("type",type);
        if(pageSize==null ||pageNo == null){
            List<Resource> resourceList = resourceService.selectList(entityWrapper);
            Map<String,Object> dataMap = Maps.newHashMap();

            dataMap.put("list",resourceList);
            return JSONResultUtil.fillResultString(1, "操作成功", dataMap);
        }else{
            List<Resource> resourceList = resourceService.selectPage(
                    new Page<Resource>(pageNo,pageSize),
                    entityWrapper
            ).getRecords();
            Map<String,Object> dataMap = Maps.newHashMap();

            dataMap.put("list",resourceList);
            return JSONResultUtil.fillResultString(1, "操作成功", dataMap);
        }
    }
    public Object getResourceOne() {
        Map<String,Object> dataMap = Maps.newHashMap();
        dataMap.put("path1",roleDaoMapper.getResourceOne());
        dataMap.put("path2",roleDaoMapper.getResourceTwo());
        return JSONResultUtil.fillResultString(1, "操作成功", dataMap );
    }

    public Object addRoleResource(String roleId, String resourceId) {
         if(roleDaoMapper.getRoleResource(roleId,resourceId)==null){
                if(roleDaoMapper.addRoleResource(roleId,resourceId)){
                    return JSONResultUtil.fillResultString(0, "操作成功", JSONObject.NULL);
                }else{
                    return JSONResultUtil.fillResultString(1, "操作失败", JSONObject.NULL);
                }
            }else{
                return JSONResultUtil.fillResultString(1, "操作失败：用户已拥有此权限", JSONObject.NULL);
            }
        }

}
