package com.asa.bilibili.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicServiceTest extends TestCase {



    @Test
    public void testRepost() {

    }

    @SpringBootApplication(scanBasePackages = "com.asa.bilibili")
    static class InnerConfig {

    }

    @Autowired
    DynamicService service;
    @Autowired
    UserService userService;



    @Test
    public void testSendDynamic() {

    }

    @Test
    public void testInstantText() throws Exception{

        Map ret = service.instantText(userService.getCredential(), "春事已不及，江行复茫然");
        System.out.println(ret);
    }

    @Test
    public void testRemoveDynamic() throws Exception{
        Map ret = service.removeDynamic(userService.getCredential(), "577581730181038275");
        System.out.println(ret);
    }
}