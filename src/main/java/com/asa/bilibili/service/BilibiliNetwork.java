package com.asa.bilibili.service;

import com.asa.bilibili.lang.NoDataResponseException;
import com.asa.bilibili.lang.RequireLoginException;
import com.asa.bilibili.lang.ResponseCodeException;
import com.asa.utils.MapUtils;
import com.asa.utils.ObjectMapUtils;
import com.asa.utils.StringUtils;
import org.jsoup.internal.StringUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
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


    public <T> T GET(String url, Map<String, Object> params, Class<T> responseTyp) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
        headers.set(HttpHeaders.REFERER,
                    "https://www.bilibili.com");
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

    public <T> T GET(String url, Class<T> responseTyp) {

        return GET(url, null, responseTyp);
    }

    public Map GET(String url, Map<String, Object> params) {

        return GET(url, params, Map.class);
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
                ret.append(uriVariable + "={" + uriVariable + "}");
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
            } else if(MapUtils.containsKey(map, "message")){
                msg = String.valueOf(MapUtils.get(map, "message"));
            }else {
                msg = "接口未返回错误信息";
            }

            // 具体异常匹配message -> 账号未登录
            if (code == -101 || StringUtils.equals(msg,"账号未登录")) {
                throw new RequireLoginException(msg);
            }
            throw new ResponseCodeException(msg);
        }
        return MapUtils.get(map, "data");
    }
}
