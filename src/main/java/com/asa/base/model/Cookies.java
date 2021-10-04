package com.asa.base.model;

import com.asa.utils.ListUtils;
import com.asa.utils.MapUtils;
import com.asa.utils.StringUtils;

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

    private Map<String, String> meta;

    public Cookies() {

        init();
    }

    private void init() {

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

    public void addCookie(String name, String value,
                          String path, String domain,
                          Date expiry, boolean isSecure,
                          boolean isHttpOnly) {

        cookies.add(new Cookie(name, value, path, domain, expiry, isSecure, isHttpOnly));
    }

    public void addCookie(String name, String value) {

        cookies.add(new Cookie(name, value));
    }

    public List<Cookie> getCookie(String name) {

        List<Cookie> ret = new ArrayList<>();
        if (ListUtils.isNotEmpty(cookies)) {
            for (Cookie c : cookies) {
                if (StringUtils.equals(name, c.getName())) {
                    ret.add(c);
                }
            }
        }
        return ret;
    }

    public Cookie getFirst(String name) {

        List<Cookie> r = getCookie(name);
        if (ListUtils.isNotEmpty(r)) {
            return r.get(0);
        }
        return null;
    }

    /**
     * 从字符串中读取
     *
     * @param str
     */
    public void readFromString(String str) {

        init();
        if (StringUtils.isNotEmpty(str)) {
            String[] cs = str.split(";");
            for (String c : cs) {
                if (StringUtils.isNotEmpty(c)) {
                    String[] kv = c.split("=");
                    if (kv.length > 0) {
                        String key = kv[0].trim();
                        String value = StringUtils.EMPTY;
                        if (kv.length > 1) {
                            value = kv[1] == null ? StringUtils.EMPTY : kv[1].trim();
                        }
                        addCookie(key, value);
                    }
                }
            }
        }
    }
}
