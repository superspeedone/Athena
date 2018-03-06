package com.superspeed.common.kit;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JsonKit.
 */
public class JsonKit {

    public static String toJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }

}

