package com.asa.weixin.spider.utils.http.handle;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class TextResponseHandle extends BaseHttpResponseHandle<String> {

    public static final TextResponseHandle DEFAULT = new TextResponseHandle();

    public TextResponseHandle() {

    }

    public TextResponseHandle(String encoding) {

        super(encoding);
    }

    @Override
    public String parse(CloseableHttpResponse response) throws IOException {

        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, getEncoding());
            EntityUtils.consume(entity);
            return result;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
