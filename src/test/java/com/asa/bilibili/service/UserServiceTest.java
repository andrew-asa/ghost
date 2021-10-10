package com.asa.bilibili.service;

import com.asa.bilibili.data.Credential;
import com.asa.base.log.LoggerFactory;
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
 * @date 2021/10/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest extends TestCase {

    @Autowired
    private UserService service;

    public void testInit() {

    }

    public void testGetSelfInfo() {

    }

    @Test
    public void testGetRelationInfo() throws Exception {

        Map map = service.getRelationInfo("77859059");
        LoggerFactory.getLogger().debug("---getRelationInfo {}--=", map);
    }

    @Test
    public void testGetCredential() {

        Credential credential = service.getCredential();
        System.out.println(credential);
    }

    @Test
    public void testGetFollowings() throws Exception{

        Credential credential = service.getCredential();
        Map f = service.getFollowings(credential.getVmid(), credential, 1);
        System.out.println(f);
    }

    @Test
    public void testGetDynamic() throws Exception{

        Map map = service.getDynamic("2435767", "0");
        System.out.println(map);
    }

    public void testTestGetDynamic() {

    }

    public void testGetSelfFollowings() {

    }

    public void testTestGetSelfFollowings() {

    }

    public void testSetCredential() {

    }

    @SpringBootApplication(scanBasePackages = "com.asa.bilibili")
    static class InnerConfig {

    }

    @Test
    public void testGetUserInfo() throws Exception {

        Map map = service.getUserInfo("77859059");
        LoggerFactory.getLogger().debug("---userinfo {}--=", map);
    }
}