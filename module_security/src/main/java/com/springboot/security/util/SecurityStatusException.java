package com.springboot.security.util;


import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author gx
 * @since 2017/5/22
 */
public class SecurityStatusException {
    static Map<String,Object> result(int status,String msg){
        return ImmutableMap.of("result",status,"errorDescription",msg);
    }

    /**
     * @return token过期
     */
    public static String tokenExpired(){
        return JSONObject.valueToString(result(-10007,"Access token is expired."));
    }

    /**
     * @return 无权访问资源
     */
    public static String accessDeny(){
        return JSONObject.valueToString(result(-10005,"AccessDenied."));
    }
}
