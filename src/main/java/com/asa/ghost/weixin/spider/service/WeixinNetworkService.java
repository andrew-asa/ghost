package com.asa.ghost.weixin.spider.service;

import com.asa.ghost.base.service.NetworkService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @author andrew_asa
 * @date 2021/11/20.
 */
@Component
public class WeixinNetworkService extends NetworkService {

    public <T> T GET(String cookie,
                     String url,
                     Map<String, Object> params,
                     Class<T> responseTyp){

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
        headers.set("cookie", cookie);
        return GET(headers,
                   url,
                   params,
                   responseTyp);
    }
}
