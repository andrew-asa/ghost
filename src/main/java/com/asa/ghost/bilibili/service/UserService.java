package com.asa.ghost.bilibili.service;

import com.asa.base.utils.MapUtils;
import com.asa.base.utils.ObjectMapUtils;
import com.asa.base.utils.ObjectMapperUtils;
import com.asa.base.utils.StringUtils;
import com.asa.base.utils.io.FileSystemResource;
import com.asa.base.utils.io.IOUtils;
import com.asa.base.utils.io.Resource;
import com.asa.ghost.bilibili.constant.Constant;
import com.asa.ghost.bilibili.data.Credential;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.asa.ghost.bilibili.constant.Constant.Page.DEFAULT_FOLLOWINGS_PAGE_SIZE;

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
     * @param uid               用户id
     * @param offset_dynamic_id 该值为第一次调用本方法时，数据中会有个 next_offset 字段，
     *                          指向下一动态列表第一条动态（类似单向链表）。
     *                          根据上一次获取结果中的 next_offset 字段值，
     *                          循环填充该值即可获取到全部动态。
     *                          0 为从头开始。
     *                          Defaults to 0.
     * @param need_top          显示置顶动态 0 不需要, 1 需要
     * @return
     */
    public Map getDynamic(String uid, String offset_dynamic_id, int need_top) throws Exception{

        String url = ObjectMapUtils.getString(api, "info", "dynamic", "url");
        Map<String, Object> params = new HashMap<>();
        params.put("host_uid", uid);
        params.put("offset_dynamic_id", offset_dynamic_id);
        params.put("need_top", need_top);
        Map dynamic = network.GET(url, params, credential);
        return network.unpackResponseMapData(dynamic);
    }

    public Map getDynamic(String uid, String offset_dynamic_id) throws Exception{

        return getDynamic(uid, offset_dynamic_id, 0);
    }

    /**
     * 获取用户的关注者列表
     *
     * @param vmid       用户id
     * @param credential 用户凭证
     * @param ps         每页大小
     * @param pn         页码
     * @param order      排序方式
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
                //credential.setSESSDATA(ObjectMapUtils.getString(map, "SESSDATA"));
                //credential.setBuvid3(ObjectMapUtils.getString(map, "buvid3"));
                //credential.setBili_jct(ObjectMapUtils.getString(map, "bili_jct"));
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
