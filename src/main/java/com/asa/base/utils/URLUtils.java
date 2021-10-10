package com.asa.base.utils;

import com.asa.base.utils.MapUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/3/27.
 */
public class URLUtils {

    /**
     * 获取参数
     * @param url
     * @return
     */
    public static Map<String, String> getParameter(String url) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            final String charset = "utf-8";
            url = URLDecoder.decode(url, charset);
            if (url.indexOf('?') != -1) {
                final String contents = url.substring(url.indexOf('?') + 1);
                String[] keyValues = contents.split("&");
                for (int i = 0; i < keyValues.length; i++) {
                    String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
                    String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 获取url参数
     * @param url
     * @param key
     * @return
     */
    public static String getParameter(String url,String key) {

        return MapUtils.get(getParameter(url), key);
    }
}
