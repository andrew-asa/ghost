package com.asa.base.net.http.handle;
import com.asa.utils.EncodeConstants;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;


public abstract class BaseHttpResponseHandle<T> {

    /**
     * 解析编码，默认为 UTF_8
     */
    private String encoding = EncodeConstants.ENCODING_UTF_8;

    public BaseHttpResponseHandle() {
    }

    public BaseHttpResponseHandle(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 获取解析编码
     *
     * @return 解析编码
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 设置解析编码
     *
     * @param encoding 解析编码
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 解析响应结果
     *
     * @param response 响应
     * @return 解析结果
     * @throws IOException io异常
     */
    public abstract T parse(CloseableHttpResponse response) throws IOException;
}
