package com.asa.bilibili.service;

import com.asa.bilibili.data.Credential;
import com.asa.bilibili.lang.NoDataResponseException;
import com.asa.bilibili.lang.ResponseCodeException;
import com.asa.utils.MapUtils;
import com.asa.utils.ObjectMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/3.
 * 推荐相关api
 */
@Component
public class RecommendService {

    @Autowired
    private BilibiliCommonService service;

    @Autowired
    private BilibiliNetwork network;

    @Autowired
    private UserService userService;

    private Map api;


    @PostConstruct
    public void init() {

        api = service.getApi("recommend");
    }

    /**
     * 获取推荐的up主
     *
     * @return
     * @throws Exception
     */
    public List getRecommendUp() throws Exception {

        String url = ObjectMapUtils.getString(api, "info", "recommend_up_user", "url");
        Map mu = network.GET(url,userService.getCredential());
        return network.unpackResponseListData(mu);
    }
}
