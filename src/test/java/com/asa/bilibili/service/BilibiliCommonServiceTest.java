package com.asa.bilibili.service;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class BilibiliCommonServiceTest extends TestCase {
    @SpringBootApplication(scanBasePackages = "com.asa.bilibili")
    static class InnerConfig {

    }

    @Autowired
    BilibiliCommonService service;

    @Test
    public void testGetApiString() {
        String url = service.getApiString("user","info","info","url");
        System.out.println(url);
    }

    public void testGetApiPath() {

    }



    @Test
    public void testGetApi() {

        Map map = service.getApi("user");
        System.out.println(map);
    }

    @Test
    public void testGetApiMap() {
        Map map = service.getApiMap("user","info","info");
        System.out.println(map);
    }
}