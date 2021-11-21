package com.asa.ghost.base.service;

import com.asa.base.utils.MapUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/28.
 * 网络相关服务
 */
public class NetworkService {

    private RestTemplate restTemplate = new RestTemplate();

    public <T> T GET(String url,
                     Map<String, Object> params,
                     Class<T> responseTyp){

        return GET(createDefaultHeader(),
                   url,
                   params,
                   responseTyp);
    }

    public <T> T GET(HttpHeaders headers,
                     String url,
                     Map<String, Object> params,
                     Class<T> responseTyp) {

        if (params == null) {
            params = new HashMap<>();
        }
        if (MapUtils.isNotEmptyMap(params)) {
            url = addURIVariables(url, params.keySet().toArray(new String[0]));
        }
        HttpEntity<T> res = restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<Map>(null, headers),
                          responseTyp, params);
        T body = res.getBody();
        return body;
    }

    public static String addURIVariables(String url, String... uriVariables) {

        StringBuffer ret = new StringBuffer();
        ret.append(url);
        if (uriVariables.length > 0) {
            ret.append("?");
            for (String uriVariable : uriVariables) {
                ret.append(uriVariable + "={" + uriVariable + "}&");
            }
        }
        return ret.toString();
    }

    protected HttpHeaders createDefaultHeader() {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
        return headers;
    }
}
