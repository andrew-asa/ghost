package com.asa.weixin.spider.service;

import com.asa.weixin.spider.model.WeixinArticlesInfo;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author andrew_asa
 * @date 2021/3/28.
 */
public class WeixinSearchServiceTest extends TestCase {

    private WeixinLoginService weixinLoginService;

    String token;
    String cookie;

    public void setUp() {
        weixinLoginService = new WeixinLoginService();
        weixinLoginService.readCookieFromPath();
        if (weixinLoginService.readCookieFromPath()) {
            token = weixinLoginService.getToken();
            cookie = weixinLoginService.getCookieString();
        }
    }

    @Test
    public void testSearchArticle() {
        WeixinSearchService searchService = new WeixinSearchService();
        String fakeId = "MzAwMzU1ODAwOQ==";
        WeixinArticlesInfo weixinArticlesInfo = searchService.searchArticle(token, cookie, fakeId, "", 0, 10);
        System.out.println();
    }

    public void testGetCommonUsedAccount() {

    }
}