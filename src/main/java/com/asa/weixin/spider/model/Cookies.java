package com.asa.weixin.spider.model;

import com.asa.utils.MapUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/3/28.
 */
public class Cookies {

    private List<Cookie> cookies;
    private Map<String,String> meta;

    public Cookies() {
        cookies = new ArrayList<>();
        meta = new HashMap<>();
    }

    public List<Cookie> getCookies() {

        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {

        this.cookies = cookies;
    }

    public Map<String, String> getMeta() {

        return meta;
    }

    public void setMeta(Map<String, String> meta) {

        this.meta = meta;
    }

    public void putMata(String key, String value) {

        MapUtils.safeAddToMap(meta, key, value);
    }

    public String getMata(String key) {

        return MapUtils.get(meta, key);
    }

    public void addCookie(  String name, String value,
                            String path, String domain,
                            Date expiry, boolean isSecure,
                            boolean isHttpOnly) {

        cookies.add(new Cookie(name, value, path, domain, expiry, isSecure, isHttpOnly));
    }
}
