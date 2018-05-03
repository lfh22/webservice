package com.springboot.security.entity;

import java.util.List;

/**
 * Created by yangyibo on 17/1/17.
 */

public class SysRole {

    private Integer id;
    private String name;
    private String role;
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

//    @Override
//    public String toString() {
//        return "SysRole{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                '}';
//    }
}
