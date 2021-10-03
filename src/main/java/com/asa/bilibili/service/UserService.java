package com.asa.bilibili.service;

import com.asa.bilibili.data.Credential;
import com.asa.utils.ObjectMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private Map api;

    private Map userInfoApi;

    @PostConstruct
    public void init() {

        api = service.getApi("user");
        userInfoApi = ObjectMapUtils.getMap(api, "info", "info");
    }

    /**
     * 获取自己的信息。如果存在缓存则使用缓存。
     *
     * @param credential
     * @return
     */
    public Map getSelfInfo(Credential credential) {

        return null;
    }

    /**
     * 获取用户信息（昵称，性别，生日，签名，头像 URL，空间横幅 URL 等）
     *
     * @param uid
     * @return
     */
    public Map getUserInfo(String uid) throws Exception {

        String url = ObjectMapUtils.getString(userInfoApi, "url");
        Map params = new HashMap();
        params.put("mid", uid);
        Map mu = network.GET(url, params);
        return network.unpackResponseMapData(mu);
    }

    /**
     * 获取用户关系信息（关注数，粉丝数，悄悄关注，黑名单数）
     *
     * @param vmid
     * @return
     */
    public Map getRelationInfo(String vmid) throws Exception {

        String url = ObjectMapUtils.getString(api, "info", "relation", "url");
        Map<String, Object> params = new HashMap<>();
        params.put("vmid", vmid);
        Map relation = network.GET(url, params);
        return network.unpackResponseMapData(relation);
    }
}
