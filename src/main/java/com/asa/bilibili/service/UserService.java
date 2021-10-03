package com.asa.bilibili.service;

import com.asa.base.utils.ObjectMapperUtils;
import com.asa.bilibili.constant.Constant;
import com.asa.bilibili.data.Credential;
import com.asa.utils.MapUtils;
import com.asa.utils.ObjectMapUtils;
import com.asa.utils.StringUtils;
import com.asa.utils.io.ClassPathResource;
import com.asa.utils.io.FileSystemResource;
import com.asa.utils.io.IOUtils;
import com.asa.utils.io.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.asa.bilibili.constant.Constant.Page.DEFAULT_FOLLOWINGS_PAGE_SIZE;

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

    private Credential credential;

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

    /**
     * 获取用户的关注者
     *
     * @param vmid
     * @param credential
     * @param ps
     * @param pn
     * @param order
     * @return
     */
    public Map getFollowings(String vmid, Credential credential, int ps, int pn, String order) throws Exception {

        String url = ObjectMapUtils.getString(api, "info", "followings", "url");
        Map<String, Object> params = new HashMap<>();
        params.put("vmid", vmid);
        params.put("ps", ps);
        params.put("pn", pn);
        if (StringUtils.equalsIgnoreCase(Constant.Order.ASC, order)) {
            params.put("order", Constant.Order.ASC);
        } else {
            params.put("order", Constant.Order.DESC);
        }
        Map followings = network.GET(url, params, credential);
        return network.unpackResponseMapData(followings);
    }

    public Map getFollowings(String vmid, Credential credential, int pn) throws Exception {

        return getFollowings(vmid, credential, DEFAULT_FOLLOWINGS_PAGE_SIZE, pn, Constant.Order.DEFAULT_ORDER);
    }

    /**
     * 获取当前用户的关注列表
     *
     * @param credential
     * @param ps
     * @param pn
     * @param order
     * @return
     */
    public Map getSelfFollowings(Credential credential, int ps, int pn, String order) throws Exception {

        return getFollowings(credential.getVmid(), credential, ps, pn, order);
    }

    public Map getSelfFollowings(Credential credential, int pn) throws Exception {

        return getSelfFollowings(credential, DEFAULT_FOLLOWINGS_PAGE_SIZE, pn, Constant.Order.DEFAULT_ORDER);
    }


    public Credential getCredential() {

        if (credential != null) {
            return credential;
        }
        InputStream in = null;
        try {
            in = getCookieStream();
            ObjectMapper mapper = ObjectMapperUtils.getDefaultMapper();
            Map map = mapper.readValue(in, Map.class);
            if (MapUtils.isNotEmptyMap(map)) {
                Credential credential = new Credential();
                credential.setVmid(ObjectMapUtils.getString(map, "vmid"));
                credential.setSESSDATA(ObjectMapUtils.getString(map, "SESSDATA"));
                credential.setBuvid3(ObjectMapUtils.getString(map, "buvid3"));
                credential.setBili_jct(ObjectMapUtils.getString(map, "bili_jct"));
                credential.setCookieStr(ObjectMapUtils.getString(map, "cookieStr"));
                setCredential(credential);
            }
        } catch (Exception e) {
            invalidCredential();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return credential;
    }

    private InputStream getCookieStream() throws Exception {

        Resource resource = new FileSystemResource("bilibili_cookie.txt");
        return resource.getInputStream();
    }

    public void invalidCredential() {

        credential = null;
    }

    public void setCredential(Credential credential) {

        this.credential = credential;
    }
}
