package com.ekabao.oil.http;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by lxj on 2016/8/6.
 */
public class PostParams {
    private HashMap<String,Object> map = new HashMap<>();
    public void add(String key,String value){
        map.put(key,value);
    }
    public void add(String key, File file){
        map.put(key,file);
    }

    public boolean isMultipart(){
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            if(value instanceof File){
                return true;
            }
        }
        return false;
    }

    public HashMap<String,Object> getParams(){
        return map;
    }
}
