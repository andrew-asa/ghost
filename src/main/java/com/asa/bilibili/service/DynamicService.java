package com.asa.bilibili.service;

import com.asa.bilibili.data.Credential;
import com.asa.utils.ListUtils;
import com.asa.base.utils.ObjectMapUtils;
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

    public Map instantText(Credential credential, String text) throws Exception {

        String url = ObjectMapUtils.getString(api, "send", "instant_text", "url");
        Map<String, Object> data = new HashMap<>();
        data.put("dynamic_id", 0);
        data.put("type", 4);
        data.put("rid", 0);
        data.put("content", text);
        data.put("extension", "{\"emoji_type\":1,\"from\":{\"emoji_type\":1},\"flag_cfg\":{}}");
        // todo
        data.put("at_uids", "[]");
        data.put("ctrl", "");
        Map ret = network.POST(url, data, credential);
        return network.unpackResponseMapData(ret);
    }

    public Object parse_at(String text) {

        return null;
    }
}
