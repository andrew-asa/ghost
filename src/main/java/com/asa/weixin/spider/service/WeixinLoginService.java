package com.asa.weixin.spider.service;

import com.alibaba.fastjson.JSONObject;
import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.log.LoggerFactory;
import com.asa.utils.StringUtils;
import com.asa.weixin.spider.model.Cookies;
import com.asa.weixin.spider.utils.URLUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@Component
public class WeixinLoginService {

    public static final String LOGIN_URL = "https://mp.weixin.qq.com/";
    public static final String TOKEN = "token";
    public static final String COOKIE_STR = "COOKIE_STR";
    private long timeOutInSeconds = 100;
    private static String cookiePath = "./weixin_cookie.txt";

    public Cookies cookies;

    private boolean fromCacheCookies;

    private boolean login;

    public boolean isFromCacheCookies() {

        return fromCacheCookies;
    }

    public void setFromCacheCookies(boolean fromCacheCookies) {

        this.fromCacheCookies = fromCacheCookies;
    }

    public WeixinLoginService() {

    }

    public void makeSureIsLogin() {

        if (!login) {
            login();
        }
    }

    public boolean isLogin() {

        return login;
    }

    public void login() {

        fromCacheCookies = readCookieFromPath();
        if (!fromCacheCookies) {
            newLogin();
        } else {
            login = true;
        }
    }

    public void newLogin() {
        EventDispatcher.fire(BrowserServiceEvent.RE_LOGIN, null);
    }




    public static enum BrowserServiceEvent implements Event<Object> {
        // 重新登录
        RE_LOGIN,
        // 二维码已经成功获取
        CREATE_QRCODE,
        // 登录成功
        LOGIN_SUCCESS,
        // 登录超时
        LOGIN_TIMEOUT,
    }

    public long getTimeOutInSeconds() {

        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(long timeOutInSeconds) {

        this.timeOutInSeconds = timeOutInSeconds;
    }


    public void savaCookie(String cookieStr, String url) {
        String token = URLUtils.getParameter(url, TOKEN);
        cookies = new Cookies();
        cookies.putMata(TOKEN,token);
        cookies.putMata(COOKIE_STR, cookieStr);
        String js = JSONObject.toJSONString(cookies);
        try {
            FileUtils.writeStringToFile(new File(cookiePath), js);
        } catch (Exception e) {
            LoggerFactory.getLogger().debug(e,"error sava cookie");
        }
    }

    public String getToken() {
        makeSureIsLogin();
        return cookies.getMata(TOKEN);
    }

    public String getCookieString() {

        return cookies.getMata(COOKIE_STR);
    }


    public boolean readCookieFromPath() {

        File file = new File(cookiePath);
        if (file.exists()) {
            String cookie = null;
            try {
                cookie = FileUtils.readFileToString(file);
                Cookies c = JSONObject.parseObject(cookie, Cookies.class);
                if (StringUtils.isNotEmpty(c.getMata(TOKEN))) {
                    cookies = c;
                    return true;
                }
            } catch (Exception e) {
                LoggerFactory.getLogger().debug("read weixin Cookie From Path fail", e);
            }
        }
        return false;
    }

    public Cookies getCookies() {

        return cookies;
    }

    public void setCookies(Cookies cookies) {

        this.cookies = cookies;
    }
}
