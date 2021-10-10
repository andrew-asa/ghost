package com.asa.bilibili.service;

import com.asa.base.utils.ObjectMapUtils;
import com.asa.bilibili.data.Credential;
import com.asa.base.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/3.
 * 动态相关操作
 */
@Component
public class DynamicService {

    @Autowired
    private BilibiliCommonService service;

    @Autowired
    private BilibiliNetwork network;

    @Autowired
    private UserService userService;

    private Map api;


    @PostConstruct
    public void init() {

        api = service.getApi("dynamic");
    }

    public Map sendDynamic(Credential credential, String text, List<InputStream> imageStreams, Date date) throws Exception {

        if (ListUtils.isEmpty(imageStreams)) {
            return instantText(credential, text);
        }
        return null;
    }

    /**
     * 插入动态文本
     *
     * @param credential
     * @param text
     * @return {"result":0,"errmsg":"; Create dynamic:577581730181038275, res:0, result:1; Push create kafka:0; Push create databus:0; Register comment result:0; Add outbox result:1","dynamic_id":577581730181038275,"create_result":1,"dynamic_id_str":"577581730181038275","_gt_":0}
     * @throws Exception
     */
    public Map instantText(Credential credential, String text) throws Exception {

        String url = ObjectMapUtils.getString(api, "send", "instant_text", "url");
        Map<String, Object> data = parseTextData(text);
        data.put("dynamic_id", 0);
        Map ret = network.POST(url, data, credential);
        return network.unpackResponseMapData(ret);
    }

    /**
     * todo 解析规则需要进一步确认
     * 解析文本
     * @param text
     * @return
     */
    public Map parseTextData(String text) {

        Map<String, Object> parseResult = new HashMap<>();
        parseResult.put("type", 4);
        parseResult.put("rid", 0);
        parseResult.put("content", text);
        parseResult.put("extension", "{\"emoji_type\":1,\"from\":{\"emoji_type\":1},\"flag_cfg\":{}}");
        // todo 解析出@ 表情等信息
        parseResult.put("at_uids", "[]");
        parseResult.put("ctrl", "");
        return parseResult;
    }

    public Map instantText(String text) throws Exception {

        return instantText(userService.getCredential(), text);
    }


    /**
     * 删除动态
     *
     * @param credential 用户凭证
     * @param dynamic_id 动态id
     * @return
     * @throws Exception
     */
    public Map removeDynamic(Credential credential, String dynamic_id) throws Exception {

        String url = ObjectMapUtils.getString(api, "operate", "delete", "url");
        Map<String, Object> data = new HashMap<>();
        data.put("dynamic_id", dynamic_id);
        Map ret = network.POST(url, data, credential);
        return network.unpackResponseMapData(ret);
    }

    /**
     * 删除动态
     *
     * @param dynamic_id
     * @return
     * @throws Exception
     */
    public Map removeDynamic(String dynamic_id) throws Exception {

        return removeDynamic(userService.getCredential(), dynamic_id);
    }

    /**
     * 转发
     *
     * @param dynamic_id
     * @param text
     * @param credential
     * @return
     * @throws Exception
     */
    public Map repost(String dynamic_id, String text, Credential credential) throws Exception {

        String url = ObjectMapUtils.getString(api, "operate", "repost", "url");
        Map<String, Object> data = parseTextData(text);
        data.put("dynamic_id", dynamic_id);
        Map ret = network.POST(url, data, credential);
        return network.unpackResponseMapData(ret);
    }
}
