package com.asa.bilibili.service;

import com.asa.utils.MapUtils;
import com.asa.utils.StringUtils;
import com.asa.utils.io.ClassPathResource;
import com.asa.utils.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
@Component
public class BilibiliCommonService {

    public static final String API_FIELD = "com/asa/bilibili/data/api/{}.json";

    public Map<String, Map> cache = new ConcurrentHashMap<>();

    public BilibiliCommonService() {

    }

    public Map getApiMap(String field, String... items) {

        Map f = getApi(field);
        Map cache = f;
        if (items.length > 0) {
            for (int i = 0; i < items.length; i++) {
                cache = (Map) cache.get(items[i]);
            }
        }
        return cache;
    }

    public String getApiString(String field, String... items) {

        Map f = getApi(field);
        Map cache = f;
        int length = items.length;
        String ret = StringUtils.EMPTY;
        if (length > 0) {
            for (int i = 0; i < length - 1; i++) {
                cache = (Map) cache.get(items[i]);
            }
            ret= String.valueOf(cache.get(items[length-1]));
        }
        return ret;
    }

    public Map getApi(String field) {

        if (cache.containsKey(field)) {
            return cache.get(field);
        }
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource pathResource = new ClassPathResource(getApiPath(field));
        InputStream in = null;
        try {
            in = pathResource.getInputStream();
            Map ret = mapper.readValue(in, Map.class);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return MapUtils.createEmptyMap();
    }

    public String getApiPath(String field) {

        return StringUtils.messageFormat(API_FIELD, field);
    }
}
