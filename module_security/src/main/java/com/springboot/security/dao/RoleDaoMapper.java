package com.springboot.security.dao;

import com.springboot.security.entity.SysRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by umakr on 2018/4/24.
 */
public interface RoleDaoMapper {

    @Insert("insert into SYS_ROLE (role, name) values (#{param1}, #{param2})")
    boolean addRole(String role, String name);

    @Select("select r.* from SYS_ROLE r")
    List<SysRole> getRoles(String pageNo, String pageSize);

    @Select("select id, name ,path from SYS_RESOURCE where type = 1")
    List<Map<String,Object>> getResourceOne();

    @Select("select id, name ,path from SYS_RESOURCE where type = 2")
    List<Map<String,Object>> getResourceTwo();

    @Select("select s.* from sys_resource_role s where s.role_id = #{param1} and s.resource_id = #{param2}" )
    Map<String,Object> getRoleResource(String roleId, String resourceId);

    @Select("select r.* from sys_resource_role s LEFT JOIN sys_resource r on s.resource_id=r.id where s.role_id = #{param1}" )
    Map<String,Object> selectRoleResource(String roleId);

    @Insert("insert into sys_resource_role (role_id,resource_id) values (#{param1}, #{param2})")
    boolean addRoleResource(String roleId, String resourceId);
}
