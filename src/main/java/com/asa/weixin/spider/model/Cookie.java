package com.asa.weixin.spider.model;

import java.util.Date;

/**
 * @author andrew_asa
 * @date 2021/3/28.
 */
public class Cookie {

    private  String name;
    private  String value;
    private  String path;
    private  String domain;
    private  Date expiry;
    private  boolean isSecure;
    private  boolean isHttpOnly;


    public Cookie(String name, String value, String path, Date expiry) {
        this(name, value, (String)null, path, expiry);
    }

    public Cookie(String name, String value, String domain, String path, Date expiry) {
        this(name, value, domain, path, expiry, false);
    }

    public Cookie(String name, String value, String domain, String path, Date expiry, boolean isSecure) {
        this(name, value, domain, path, expiry, isSecure, false);
    }

    public Cookie(String name, String value, String domain, String path, Date expiry, boolean isSecure, boolean isHttpOnly) {
        this.name = name;
        this.value = value;
        this.path = path != null && !"".equals(path) ? path : "/";
        this.domain = stripPort(domain);
        this.isSecure = isSecure;
        this.isHttpOnly = isHttpOnly;
        if (expiry != null) {
            this.expiry = new Date(expiry.getTime() / 1000L * 1000L);
        } else {
            this.expiry = null;
        }

    }

    public Cookie(String name, String value) {
        this(name, value, "/", (Date)null);
    }

    public Cookie(String name, String value, String path) {
        this(name, value, path, (Date)null);
    }

    private static String stripPort(String domain) {
        return domain == null ? null : domain.split(":")[0];
    }

    public void validate() {
        if (this.name != null && !"".equals(this.name) && this.value != null && this.path != null) {
            if (this.name.indexOf(59) != -1) {
                throw new IllegalArgumentException("Cookie names cannot contain a ';': " + this.name);
            } else if (this.domain != null && this.domain.contains(":")) {
                throw new IllegalArgumentException("Domain should not contain a port: " + this.domain);
            }
        } else {
            throw new IllegalArgumentException("Required attributes are not set or any non-null attribute set to null");
        }
    }

    public Cookie() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    public String getDomain() {

        return domain;
    }

    public void setDomain(String domain) {

        this.domain = domain;
    }

    public Date getExpiry() {

        return expiry;
    }

    public void setExpiry(Date expiry) {

        this.expiry = expiry;
    }

    public boolean isSecure() {

        return isSecure;
    }

    public void setSecure(boolean secure) {

        isSecure = secure;
    }

    public boolean isHttpOnly() {

        return isHttpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {

        isHttpOnly = httpOnly;
    }
}
