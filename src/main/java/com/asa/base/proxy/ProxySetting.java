package com.asa.base.proxy;

/**
 * @author andrew_asa
 * @date 2021/10/1.
 */
public class ProxySetting {

    public static void setUpProxy() {
        String proxyHost = "127.0.0.1";
        String proxyPort = "1080";
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        // 对https也开启代理
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);
    }

    public static void removeProxy() {
        String proxyHost = "";
        String proxyPort = "";
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        // 对https也开启代理
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);
    }
}
