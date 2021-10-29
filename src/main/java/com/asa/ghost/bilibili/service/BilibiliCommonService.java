package com.asa.ghost.bilibili.service;


import com.asa.base.utils.StringUtils;
import com.asa.ghost.base.service.ApiService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
@Component
public class BilibiliCommonService extends ApiService {

    public static final String API_FIELD = "com/asa/ghost/bilibili/data/api/{}.json";

    public Map<String, Map> cache = new ConcurrentHashMap<>();

    public BilibiliCommonService() {

    }

    public String getApiPath(String field) {

        return StringUtils.messageFormat(API_FIELD, field);
    }
}
