package com.asa.bilibili.service;

import com.asa.bilibili.data.Credential;
import org.junit.Test;

/**
 * @author andrew_asa
 * @date 2021/10/26.
 */
public class UserServiceTest2 {

    @Test
    public void testGet() {
        UserService userService = new UserService();
        Credential credential = userService.getCredential();
    }
}
