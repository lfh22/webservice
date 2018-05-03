package com.springboot.security.util;

import org.json.JSONObject;

/**
 * Created by umakr on 2018/4/20.
 */
public class JSONResultUtil {
    public static String fillResultString(Integer status, String message, Object result){
        JSONObject jsonObject = new JSONObject(){{
            put("status", status);
            put("message", message);
            put("data", result);
        }};
        return jsonObject.toString();
    }
}
