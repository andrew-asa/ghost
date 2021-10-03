package com.asa.bilibili.data;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
public class Credential {

    /**
     * 当前用户id
     */
    private String vmid;

    private String SESSDATA;

    private String buvid3;

    private String bili_jct;

    private String cookieStr;

    public String getSESSDATA() {

        return SESSDATA;
    }

    public void setSESSDATA(String SESSDATA) {

        this.SESSDATA = SESSDATA;
    }

    public String getBuvid3() {

        return buvid3;
    }

    public void setBuvid3(String buvid3) {

        this.buvid3 = buvid3;
    }

    public String getBili_jct() {

        return bili_jct;
    }

    public void setBili_jct(String bili_jct) {

        this.bili_jct = bili_jct;
    }

    public Map<String, String> getCookies() {

        Map<String, String> cookie = new HashMap<>();
        cookie.put("SESSDATA", SESSDATA);
        cookie.put("buvid3", buvid3);
        cookie.put("bili_jct", bili_jct);
        return cookie;
    }

    public String getCookiesStr() {

        return cookieStr;
    }

    public String getCookieStr() {

        return cookieStr;
    }

    public void setCookieStr(String cookieStr) {

        this.cookieStr = cookieStr;
    }

    public String getVmid() {

        return vmid;
    }

    public void setVmid(String vmid) {

        this.vmid = vmid;
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", Credential.class.getSimpleName() + "[", "]")
                .add("vmid='" + vmid + "'")
                .add("SESSDATA='" + SESSDATA + "'")
                .add("buvid3='" + buvid3 + "'")
                .add("bili_jct='" + bili_jct + "'")
                .add("cookieStr='" + cookieStr + "'")
                .toString();
    }
}
