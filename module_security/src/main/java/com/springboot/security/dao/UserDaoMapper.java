package com.springboot.security.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springboot.security.entity.SysRole;
import com.springboot.security.entity.SysUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface UserDaoMapper extends BaseMapper<SysUser> {
    @Select("select r.* from sys_role_user s LEFT JOIN Sys_Role r on s.Sys_Role_id=r.id where s.Sys_User_id= #{userId}")
    List<SysRole> findRolesByUserId(Long userId);


    @Select("select u.id userId,u.* from Sys_User u where username= #{username}" )
    @Results({
            @Result( property = "id",column = "userId"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "com.springboot.security.dao.UserDaoMapper.findRolesByUserId"))
    })
    SysUser findByUserName(String username);

    @Select("select u.id userId ,u.* ,r.name\n" +
            "        from Sys_User u\n" +
            "        LEFT JOIN sys_role_user sru on u.id= sru.Sys_User_id\n" +
            "        LEFT JOIN Sys_Role r on sru.Sys_Role_id=r.id\n" +
            "        where username= #{param1} and password= #{param2}")
    @Results({
            @Result( property = "id",column = "userId"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "com.springboot.security.dao.UserDaoMapper.findRolesByUserId"))
    })
    SysUser findByUserNameAndPassWord(String username, String password);

    @Select("select u.id userId,u.* from Sys_User u where id= #{id}" )
    @Results({
            @Result( property = "id",column = "userId"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "com.springboot.security.dao.UserDaoMapper.findRolesByUserId"))
    })
    Map<String,Object> getUserDetailsByUserId(Long id);



    @Insert("insert into SYS_USER (username,password,account,email) values (#{param1}, #{param2},#{param3},#{param4})")
    boolean addUser(String username, String password, String account, String email);

    @Select("select u.id userId,u.* from Sys_User u" )
    @Results({
            @Result( property = "id",column = "userId"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "com.springboot.security.dao.UserDaoMapper.findRolesByUserId"))
    })
    List<SysUser> fetchUser(int pageNo, int pageSize);

    @Select("select u.id userId,u.* from Sys_User u" )
    @Results({
            @Result( property = "id",column = "userId"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "com.springboot.security.dao.UserDaoMapper.findRolesByUserId"))
    })
    List<SysUser> fetchUserRole();

    @Select("select s.* from sys_role_user s where s.Sys_Role_id = #{param1} and s.Sys_User_id = #{param2}" )
    Map<String,Object> getUserRole(int roleId, int userId);

    @Insert("insert into sys_role_user (Sys_Role_id,Sys_User_id) values (#{param1}, #{param2})")
    boolean addUserRole(int roleId, int userId);


    @Select("select * from sys_resource WHERE pid = #{id}" )
//    @Select("SELECT re.* from sys_role_user r \n" +
//        "RIGHT JOIN sys_resource_role rr on rr.role_id = r.Sys_Role_id \n" +
//        "LEFT JOIN sys_resource re on re.id = rr.resource_id\n" +
//        "where r.Sys_User_id = #{userId} and re.type = 1 and pid = #{id}" )
    List<Map<String,Object>> getResource(Long id);

    @Select("SELECT re.* from sys_role_user r \n" +
            "RIGHT JOIN sys_resource_role rr on rr.role_id = r.Sys_Role_id \n" +
            "LEFT JOIN sys_resource re on re.id = rr.resource_id\n" +
            "where r.Sys_User_id = #{userId} and re.type = 1" )
    @Results({
            @Result(property = "children", column = "id",
                    many = @Many(select = "com.springboot.security.dao.UserDaoMapper.getResource"))
    })
    List<Map<String,Object>> ShowMenuByUserId(Long userId);
}
