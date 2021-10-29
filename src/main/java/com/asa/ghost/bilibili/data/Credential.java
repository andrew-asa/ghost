package com.asa.ghost.bilibili.data;

import com.asa.base.model.Cookie;
import com.asa.base.model.Cookies;
import com.asa.base.utils.StringUtils;

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

    //private String SESSDATA;
    //
    //private String buvid3;
    //
    private String bili_jct;

    private String cookieStr;

    private Cookies cookies;

    //public String getSESSDATA() {
    //
    //    return SESSDATA;
    //}
    //
    //public void setSESSDATA(String SESSDATA) {
    //
    //    this.SESSDATA = SESSDATA;
    //}
    //
    //public String getBuvid3() {
    //
    //    return buvid3;
    //}
    //
    //public void setBuvid3(String buvid3) {
    //
    //    this.buvid3 = buvid3;
    //}
    //
    public String getBili_jct() {

        String ret = StringUtils.EMPTY;
        if (cookies != null) {
            Cookie cookie = cookies.getFirst("bili_jct");
            if (cookie != null) {
                ret = cookie.getValue();
            }
        }
        return ret;
    }
    //
    //public void setBili_jct(String bili_jct) {
    //
    //    this.bili_jct = bili_jct;
    //}

    public Cookies getCookies() {

        return cookies;
    }

    public String getCookiesStr() {

        return cookieStr;
    }

    public String getCookieStr() {

        return cookieStr;
    }

    public void setCookieStr(String cookieStr) {

        if (StringUtils.isNotEmpty(cookieStr)) {
            cookies = new Cookies();
            cookies.readFromString(cookieStr);
        }
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
                //.add("SESSDATA='" + SESSDATA + "'")
                //.add("buvid3='" + buvid3 + "'")
                //.add("bili_jct='" + bili_jct + "'")
                .add("cookieStr='" + cookieStr + "'")
                .toString();
    }
}
