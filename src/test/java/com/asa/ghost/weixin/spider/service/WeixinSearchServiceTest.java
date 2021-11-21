package com.asa.ghost.weixin.spider.service;

import com.asa.base.log.LoggerFactory;
import com.asa.ghost.weixin.spider.model.WeixinArticlesInfo;
import com.asa.ghost.weixin.spider.model.WeixinPublicAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/3/28.
 */
public class WeixinSearchServiceTest extends TestCase {

    private WeixinLoginService weixinLoginService;
    private WeixinSearchService searchService ;

    String token;
    String cookie;

    public void setUp() {
        weixinLoginService = new WeixinLoginService();
        weixinLoginService.readCookieFromPath();
        if (weixinLoginService.readCookieFromPath()) {
            token = weixinLoginService.getToken();
            cookie = weixinLoginService.getCookieString();
        }
        searchService = new WeixinSearchService();
        searchService.setApiService(new WeixinApiService());
        searchService.setNetworkService(new WeixinNetworkService());
    }

    @Ignore
    @Test
    public void testSearchArticle() {

        String fakeId = "MzAwMzU1ODAwOQ==";
        WeixinArticlesInfo weixinArticlesInfo = searchService.searchArticle(token, cookie, fakeId, "", 0, 10);
        System.out.println();
    }

    @Ignore
    @Test
    public void testSearchArticle2() {

        String fakeId = "MzAwMzU1ODAwOQ==";
        WeixinArticlesInfo weixinArticlesInfo = searchService.searchArticle(token, cookie, fakeId, "", 0, 10);
        System.out.println();
    }

    @Ignore
    @Test
    public void testSearchArticle3() throws Exception{

        String fakeId = "MzAwMzU1ODAwOQ==";
        Map<String, WeixinArticlesInfo> re = new HashMap<>();
        WeixinSearchService.SEARCH_ARTICLE_TYPE = 1;
        WeixinArticlesInfo weixinArticlesInfo = searchService.searchArticle(token, cookie, fakeId, "", 0, 10);
        re.put("1", weixinArticlesInfo);
        WeixinSearchService.SEARCH_ARTICLE_TYPE = 2;
        WeixinArticlesInfo weixinArticlesInfo2 = searchService.searchArticle(token, cookie, fakeId, "", 0, 10);
        re.put("2", weixinArticlesInfo2);
        WeixinSearchService.SEARCH_ARTICLE_TYPE = 3;
        WeixinArticlesInfo weixinArticlesInfo3 = searchService.searchArticle(token, cookie, fakeId, "", 0, 10);
        re.put("3", weixinArticlesInfo3);
        LoggerFactory.getLogger().debug(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(re));
    }

    @Test
    public void testGetCommonUsedAccount() {

        List<WeixinPublicAccount> accounts = searchService.searchPublicAccount("政事堂", token, cookie);
        LoggerFactory.getLogger().debug("{}",accounts);
    }
}