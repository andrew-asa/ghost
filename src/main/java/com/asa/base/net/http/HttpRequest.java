package com.asa.base.net.http;

import com.asa.utils.EncodeConstants;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;

import java.util.Collections;
import java.util.Map;

public class HttpRequest {

    private static final int TIME_OUT = 10 * 1000;
    private static final RequestConfig DEFAULT = RequestConfig
            .custom()
            .setConnectionRequestTimeout(TIME_OUT)
            .setConnectTimeout(TIME_OUT)
            .setSocketTimeout(TIME_OUT)
            .build();
    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 请求参数
     */
    private Map<String, String> params;

    /**
     * 请求参数
     *
     * @see RequestConfig
     */
    private RequestConfig config;

    /**
     * 请求参数
     *
     * @see HttpEntity
     */
    private HttpEntity httpEntity;

    /**
     * 请求方法
     */
    private HttpRequestType method;

    /**
     * 参数字符集
     */
    private String encoding;

    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.headers = builder.headers;
        this.params = builder.params;
        this.config = builder.config;
        this.encoding = builder.encoding;
        this.httpEntity = builder.httpEntity;
        this.method = builder.method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public RequestConfig getConfig() {
        return config;
    }

    public String getEncoding() {
        return encoding;
    }

    public HttpEntity getHttpEntity() {
        return httpEntity;
    }

    public HttpRequestType getMethod() {
        return method;
    }

    public static Builder custom() {
        return new Builder();
    }

    public static final class Builder {
        private String url;
        private Map<String, String> headers = Collections.emptyMap();
        private Map<String, String> params = Collections.emptyMap();
        private RequestConfig config = DEFAULT;
        private HttpEntity httpEntity;
        private String encoding = EncodeConstants.ENCODING_UTF_8;
        private HttpRequestType method = HttpRequestType.GET;

        private Builder() {
        }

        public HttpRequest build() {
            if (this.url == null) {
                throw new IllegalStateException("url == null");
            }
            return new HttpRequest(this);
        }

        public Builder url(String url) {
            if (url == null) {
                throw new NullPointerException("url == null");
            }
            this.url = url;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            if (headers != null) {
                this.headers = headers;
            }
            return this;
        }

        public Builder params(Map<String, String> params) {
            if (params != null) {
                this.params = params;
            }
            return this;
        }

        public Builder config(RequestConfig config) {
            this.config = config;
            return this;
        }

        public Builder get() {
            this.method = HttpRequestType.GET;
            return this;
        }

        public Builder post(HttpEntity httpEntity) {
            this.method = HttpRequestType.POST;
            this.httpEntity(httpEntity);
            return this;
        }

        public Builder post(Map<String, String> params) {
            this.method = HttpRequestType.POST;
            this.params(params);
            return this;
        }

        public Builder put(HttpEntity httpEntity) {
            this.method = HttpRequestType.PUT;
            this.httpEntity(httpEntity);
            return this;
        }

        public Builder put(Map<String, String> params) {
            this.method = HttpRequestType.PUT;
            this.params(params);
            return this;
        }

        public Builder delete() {
            this.method = HttpRequestType.DELETE;
            return this;
        }

        public Builder encoding(String encoding) {
            if (encoding == null) {
                throw new NullPointerException("httpEntity == null");
            }
            this.encoding = encoding;
            return this;
        }

        public Builder httpEntity(HttpEntity httpEntity) {
            this.httpEntity = httpEntity;
            return this;
        }

        public Builder method( HttpRequestType method) {
            if (method == null) {
                throw new NullPointerException("method == null");
            }
            this.method = method;
            return this;
        }
    }
}
