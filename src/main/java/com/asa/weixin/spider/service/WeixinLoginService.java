package com.asa.weixin.spider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.log.LoggerFactory;
import com.asa.utils.ListUtils;
import com.asa.utils.MapUtils;
import com.asa.utils.StringUtils;
import com.asa.weixin.spider.model.Cookie;
import com.asa.weixin.spider.model.Cookies;
import com.asa.weixin.spider.utils.URLUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@Component
public class WeixinLoginService {

    public static final String LOGIN_URL = "https://mp.weixin.qq.com/";

    public static final String TOKEN = "token";
    public static final String COOKIE_STR = "COOKIE_STR";

    private WebDriver browser;

    private long timeOutInSeconds = 100;

    private byte[] qrcodeBytes;

    private boolean hadCreateQrcodeBytes = false;

    private String cookiePath = "./weixin_cookie.txt";

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
        new Thread(() -> {
            startDriver();
        }).start();
    }



    @PreDestroy
    public void destroy() {

        if (browser != null) {
            browser.close();
        }
    }

    public static enum BrowserServiceEvent implements Event<Object> {
        // 二维码已经成功获取
        CREATE_QRCODE,
        // 登录成功
        LOGIN_SUCCESS,
        // 登录超时
        LOGIN_TIMEOUT,
    }


    public void startDriver() {

        try {
            browser = new ChromeDriver();
            browser.get(LOGIN_URL);
            Thread.sleep(1000);
            qrcodeBytes = browser.findElement(By.xpath("//img[@class='login__type__container__scan__qrcode']"))
                    .getScreenshotAs(OutputType.BYTES);
            hadCreateQrcodeBytes = true;
            EventDispatcher.fire(BrowserServiceEvent.CREATE_QRCODE, qrcodeBytes);
            WebDriverWait wait = new WebDriverWait(browser, timeOutInSeconds);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='weui-desktop-account__info']")));
            boolean status = element.isDisplayed();
            // 判断
            if (status) {
                EventDispatcher.fire(BrowserServiceEvent.LOGIN_SUCCESS, qrcodeBytes);
                savaCookie(browser);
                System.out.println("===== 成功登录 ======");
            } else {
                EventDispatcher.fire(BrowserServiceEvent.LOGIN_TIMEOUT, qrcodeBytes);
                System.out.println("===== 成功超时======");
            }
        } catch (Exception e) {
            LoggerFactory.getLogger().error(e,"error startDriver");
        }finally {
            browser.close();
        }
    }

    public boolean isHadCreateQrcodeBytes() {

        return hadCreateQrcodeBytes;
    }

    public void setHadCreateQrcodeBytes(boolean hadCreateQrcodeBytes) {

        this.hadCreateQrcodeBytes = hadCreateQrcodeBytes;
    }

    public WebDriver getBrowser() {

        return browser;
    }

    public void setBrowser(WebDriver browser) {

        this.browser = browser;
    }

    public long getTimeOutInSeconds() {

        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(long timeOutInSeconds) {

        this.timeOutInSeconds = timeOutInSeconds;
    }

    public byte[] getQrcodeBytes() {

        return qrcodeBytes;
    }

    public void setQrcodeBytes(byte[] qrcodeBytes) {

        this.qrcodeBytes = qrcodeBytes;
    }


    public void savaCookie(Set<org.openqa.selenium.Cookie> cs,String url) {
        Cookies cookies = new Cookies();
        String token = URLUtils.getParameter(url, TOKEN);
        cookies.putMata(TOKEN,token);
        if (cs != null && !cs.isEmpty()) {
            cs.forEach(i -> {
                cookies.addCookie(i.getName(),i.getValue(),
                                  i.getPath(),i.getDomain(),
                                  i.getExpiry(),i.isSecure(),
                                  i.isHttpOnly());
            });
            cookies.putMata(COOKIE_STR, createCookieStr(cs));
        }
        String js = JSONObject.toJSONString(cookies);
        try {
            FileUtils.writeStringToFile(new File(cookiePath), js);
        } catch (Exception e) {
            LoggerFactory.getLogger().debug(e,"error sava cookie");
        }
    }

    public void savaCookie(WebDriver browser) {

        Set<org.openqa.selenium.Cookie> cs = browser.manage().getCookies();
        String url = browser.getCurrentUrl();
        savaCookie(cs,url);
    }

    public String createCookieStr(Set<org.openqa.selenium.Cookie> cookies) {

        StringBuilder sb = new StringBuilder();
        if (cookies != null && cookies.size() > 0) {
            int s = cookies.size();
            s--;
            org.openqa.selenium.Cookie[] cs = cookies.toArray(new org.openqa.selenium.Cookie[0]);
            for (int i = 0; i < s; i++) {
                org.openqa.selenium.Cookie c = cs[i];
                sb.append(c.getName() + "=").append(c.getValue()==null?"":c.getValue());
                sb.append("; ");
            }
            org.openqa.selenium.Cookie c = cs[s];
            sb.append(c.getName() + "=").append(c.getValue()==null?"":c.getValue());
        }
        return sb.toString();
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
