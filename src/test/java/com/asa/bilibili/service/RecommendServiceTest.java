package com.asa.bilibili.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author andrew_asa
 * @date 2021/10/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendServiceTest extends TestCase {

    @SpringBootApplication(scanBasePackages = "com.asa.bilibili")
    static class InnerConfig {

    }

    @Autowired
    RecommendService service;

    @Test
    public void testGetRecommendUp() throws Exception {

        List ups = service.getRecommendUp();
        System.out.println(ups);
    }
}