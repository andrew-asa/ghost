package com.asa.bilibili.service;

import com.asa.utils.ObjectMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
@Component
public class UserService {

    @Autowired
    private BilibiliCommonService service;

    @Autowired
    private BilibiliNetwork network;

    private Map api ;
    private Map userInfoApi;

    @PostConstruct
    public void init() {

        api = service.getApi("user");
        userInfoApi = ObjectMapUtils.getMap(api, "info", "info");
    }

    public Map getUserInfo(String uid) {

        User u = new User();
        String url = ObjectMapUtils.getString(userInfoApi, "url");
        Map params = new HashMap();
        params.put("mid", uid);
        Map mu = network.GET(url, params);
        return mu;
    }
}
