package com.asa.ghost.weixin.spider.service;

import junit.framework.TestCase;

/**
 * @author andrew_asa
 * @date 2021/11/20.
 */
public class WeixinApiServiceTest extends TestCase {

    public void testGetApiPath() {
        WeixinApiService apiService = new WeixinApiService();
        System.out.println(apiService.getApiString("article", "info", "article", "url"));
    }
}