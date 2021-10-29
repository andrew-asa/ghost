package com.asa.ghost.bilibili.service;

import com.asa.base.utils.MapUtils;
import com.asa.base.utils.StringUtils;
import com.asa.ghost.bilibili.data.Credential;
import com.asa.ghost.bilibili.lang.NoDataResponseException;
import com.asa.ghost.bilibili.lang.RequireLoginException;
import com.asa.ghost.bilibili.lang.ResponseCodeException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
@Component
public class BilibiliNetwork {

    private RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders customRequireHeader(Credential credential) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
        headers.set(HttpHeaders.REFERER,
                    "https://www.bilibili.com");
        if (credential != null) {
            headers.set(HttpHeaders.COOKIE, credential.getCookieStr());
        }
        return headers;
    }

    public <T> T POST(String url, Map<String, Object> data,
                      Credential credential,
                      Class<T> responseTyp) {

        HttpHeaders headers = customRequireHeader(credential);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String,Object> sendData = new LinkedMultiValueMap<>();
        if (MapUtils.isNotEmptyMap(data)) {
            sendData.setAll(data);
        }
        if (credential != null) {
            sendData.add("csrf", credential.getBili_jct());
            sendData.add("csrf_token", credential.getBili_jct());
        }
        HttpEntity request = new HttpEntity(sendData, headers);
        HttpEntity<T> res = restTemplate.postForEntity(url, request, responseTyp);
        T body = res.getBody();
        return body;
    }

    public Map POST(String url, Map<String, Object> data,
                    Credential credential) {

        return POST(url, data, credential, Map.class);
    }


    public <T> T GET(String url, Map<String, Object> params,
                     Credential credential,
                     Class<T> responseTyp) {

        HttpHeaders headers = customRequireHeader(credential);
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

    public <T> T GET(String url, Credential credential, Class<T> responseTyp) {

        return GET(url, (Map<String, Object>) null, (Credential) null, responseTyp);
    }

    public <T> T GET(String url, Class<T> responseTyp) {

        return GET(url, (Credential) null, responseTyp);
    }

    public Map GET(String url, Map<String, Object> params, Credential credential) {

        return GET(url, params, credential, Map.class);
    }

    public Map GET(String url, Map<String, Object> params) {

        return GET(url, params, (Credential) null, Map.class);
    }

    public Map GET(String url, Credential credential) {

        return GET(url, (Map<String, Object>) null, credential, Map.class);
    }

    public Map GET(String url) {

        return GET(url, (Map<String, Object>) null);
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

    public List unpackResponseListData(Map map) throws Exception {

        return (List) unpackResponseData(map);
    }

    public Map unpackResponseMapData(Map map) throws Exception {

        return (Map) unpackResponseData(map);
    }

    /**
     * 获取从后端获取的data数据
     *
     * @param map
     * @return
     * @throws Exception
     */
    public Object unpackResponseData(Map map) throws Exception {

        if (map == null) {
            throw new NoDataResponseException("null data unpack");
        }
        int code = MapUtils.getInteger(map, "code", -1);
        if (code != 0) {

            // 解析错误msg/message
            String msg = StringUtils.EMPTY;
            if (MapUtils.containsKey(map, "msg")) {
                msg = String.valueOf(MapUtils.get(map, "msg"));
            } else if (MapUtils.containsKey(map, "message")) {
                msg = String.valueOf(MapUtils.get(map, "message"));
            } else {
                msg = "接口未返回错误信息";
            }

            // 具体异常匹配message -> 账号未登录
            if (code == -101 || StringUtils.equals(msg, "账号未登录")) {
                throw new RequireLoginException(msg);
            }
            throw new ResponseCodeException(msg);
        }
        return MapUtils.get(map, "data");
    }

    public Map<String, String> strToCookies(String str) {

        Map ret = new HashMap();
        if (StringUtils.isNotEmpty(str)) {
            String[] cookies = str.split(";");
        }
        return ret;
    }
}
