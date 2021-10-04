package com.asa.bilibili.service;

import com.asa.bilibili.data.Credential;
import junit.framework.TestCase;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
public class BilibiliNetworkTest extends TestCase {

    public void testGET() {

        BilibiliNetwork network = new BilibiliNetwork();
        Map g = network.GET("https://api.bilibili.com/x/space/acc/info?mid=77859059", new HashMap());
        System.out.println(g);
    }

    public void testGET2() {

        BilibiliNetwork network = new BilibiliNetwork();
        Map params = new HashMap();
        params.put("mid", 77859059);
        Map g = network.GET("https://api.bilibili.com/x/space/acc/info", params);
        System.out.println(g);
    }

    public void testAddURIVariables() {

        String url = BilibiliNetwork.addURIVariables("https://api.bilibili.com/x/space/acc/info", "mid");
        System.out.println(url);
    }

    public void testPOST() throws Exception {

        String url = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/create";
        UserService userService = new UserService();
        Credential credential = userService.getCredential();
        String text = "无处话秋凉";
        MultiValueMap<String,Object> data = new LinkedMultiValueMap<>();
        data.add("dynamic_id", 0);
        data.add("type", 4);
        data.add("rid", 0);
        data.add("content", text);
        data.add("extension", "{\"emoji_type\":1,\"from\":{\"emoji_type\":1},\"flag_cfg\":{}}");
        data.add("at_uids", "");
        data.add("ctrl", "");
        data.add("csrf", credential.getBili_jct());
        data.add("csrf_token", credential.getBili_jct());


        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Safari/605.1.15");
        headers.set(HttpHeaders.COOKIE, credential.getCookieStr());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        HttpEntity request = new HttpEntity(data, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity map = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        System.out.println(map);
    }
}