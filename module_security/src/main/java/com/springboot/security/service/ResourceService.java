package com.springboot.security.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.springboot.security.dao.ResourceMapper;
import com.springboot.security.dao.RoleDaoMapper;
import com.springboot.security.entity.Resource;
import com.springboot.security.util.JSONResultUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by umakr on 2018/4/24.
 */
@Service
public class ResourceService extends ServiceImpl<ResourceMapper, Resource> {

}
