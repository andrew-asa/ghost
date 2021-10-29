package com.asa.base.ui.browser.base.service;

import com.asa.base.utils.MapUtils;
import com.asa.base.utils.StringUtils;
import com.asa.base.utils.io.ClassPathResource;
import com.asa.base.utils.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andrew_asa
 * @date 2021/10/28.
 */
public abstract class ApiService {

    public Map<String, Map> cache = new ConcurrentHashMap<>();

    public ApiService() {

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

    public abstract String getApiPath(String field);
}
