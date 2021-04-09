package com.asa.weixin.spider.utils.http;


import com.asa.log.LoggerFactory;
import com.asa.utils.EncodeConstants;
import com.asa.weixin.spider.utils.http.handle.BaseHttpResponseHandle;
import com.asa.weixin.spider.utils.http.handle.TextResponseHandle;
import com.asa.weixin.spider.utils.http.handle.UploadResponseHandle;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpToolbox {

    private static final int RETRY_TIMES = 5;

    private static CloseableHttpClient httpClient = null;

    private final static Object SYNC_LOCK = new Object();

    /**
     * 根据请求地址创建HttpClient对象
     *
     * @param url 请求地址
     * @return HttpClient对象
     */
    public static CloseableHttpClient getHttpClient(String url) {
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        CloseableHttpClient temp = httpClient;
        if (temp == null) {
            synchronized (SYNC_LOCK) {
                temp= httpClient;
                if (temp == null) {
                    temp = createHttpClient(hostname, port, SSLContexts.createDefault());
                    httpClient = temp;
                }
            }
        }
        return httpClient;
    }


    public static CloseableHttpClient createHttpClient(String hostname, int port, SSLContext sslContext) {
        return createHttpClient(200, 40, 100, hostname, port, sslContext);
    }

    private static CloseableHttpClient createHttpClient(int maxTotal,
                                                        int maxPerRoute,
                                                        int maxRoute,
                                                        String hostname,
                                                        int port,
                                                        SSLContext sslContext) {
        ConnectionSocketFactory socketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", socketFactory)
                .register("https", sslConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= RETRY_TIMES) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                org.apache.http.HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                return !(request instanceof HttpEntityEnclosingRequest);
            }
        };

        return HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
    }

    /**
     * 设置 httpEntity
     *
     * @param requestBase 请求体
     * @param httpRequest 请求
     */
    private static void setHttpEntity( HttpEntityEnclosingRequestBase requestBase,  HttpRequest httpRequest) {
        HttpEntity httpEntity = httpRequest.getHttpEntity();
        if (httpEntity != null) {
            // 如果存在 httpEntity 直接设置
            requestBase.setEntity(httpEntity);
            return;
        }
        Map<String, String> params = httpRequest.getParams();
        if (params == null || params.isEmpty()) {
            return;
        }
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            requestBase.setEntity(new UrlEncodedFormEntity(pairs, httpRequest.getEncoding()));
        } catch (UnsupportedEncodingException e) {
            LoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static <V> Map<String, String> transformMap(Map<String, V> oldMap) {
        if (oldMap == null) {
            return null;
        }
        return Maps.transformEntries(oldMap, new Maps.EntryTransformer<String, V, String>() {
            @Override
            public String transformEntry( String key,  V value) {
                return value == null ? null : value.toString();
            }
        });
    }

    /**
     * 发起POST请求并获取返回的文本
     *
     * @param url    响应请求的的服务器地址
     * @param params POST请求的参数
     * @return 服务器返回的文本内容
     */
    public static <V> String post(String url, Map<String, V> params) throws IOException {
        return executeAndParse(HttpRequest
                .custom()
                .url(url)
                .post(transformMap(params))
                .build());
    }

    /**
     * 发起GET请求并获取返回的文本
     *
     * @param url 响应请求的的服务器地址
     * @return 服务器返回的文本内容
     */
    public static String get(String url) throws IOException {
        return executeAndParse(HttpRequest.custom().url(url).build());
    }

    /**
     * 发起GET请求并获取返回的文本
     *
     * @param url    响应请求的的服务器地址
     * @param params 参数
     * @return 服务器返回的文本内容
     */
    public static String get(String url, Map<String, String> params) throws IOException {
        return executeAndParse(HttpRequest.custom().url(url).params(params).build());
    }


    /**
     * 上传文件到指定的服务器
     *
     * @param url             接收文件的服务器地址
     * @param reqEntity       请求实体
     * @param charset         文件的编码
     * @param headers         请求头
     * @param httpRequestType 请求类型
     * @throws IOException 上传中出现错误则抛出此异常
     */
    public static void upload(String url, HttpEntity reqEntity, Charset charset, Map<String, String> headers, HttpRequestType httpRequestType) throws IOException {
        executeAndParse(HttpRequest
                        .custom()
                        .url(url)
                        .headers(headers)
                        .method(httpRequestType)
                        .httpEntity(reqEntity)
                        .encoding(charset.toString())
                        .build(),
                        UploadResponseHandle.DEFAULT);
    }



    /**
     * 请求资源或服务，使用默认文本http解析器，UTF-8编码
     *
     * @param httpRequest httpRequest
     * @return 返回处理结果
     */
    public static String executeAndParse(HttpRequest httpRequest) throws IOException {
        return executeAndParse(httpRequest, TextResponseHandle.DEFAULT);
    }

    /**
     * 请求资源或服务，自请求参数，并指定 http 响应处理器
     * 例：
     * <pre>
     *      String res = HttpToolbox.executeAndParse(HttpRequest
     *              .custom()
     *              .url("")
     *              .build(),
     *          TextResponseHandle.DEFAULT);
     * </pre>
     *
     * @param httpRequest httpRequest
     * @param handle      http 解析器
     * @return 返回处理结果
     */
    public static <T> T executeAndParse(HttpRequest httpRequest, BaseHttpResponseHandle<T> handle) throws IOException {
        return handle.parse(execute(httpRequest));
    }

    /**
     * 请求资源或服务，传入请求参数
     *
     * @param httpRequest httpRequest
     * @return 返回处理结果
     */
    public static CloseableHttpResponse execute(HttpRequest httpRequest) throws IOException {
        return execute(getHttpClient(httpRequest.getUrl()), httpRequest);
    }

    /**
     * 请求资源或服务，自定义client对象，传入请求参数
     *
     * @param httpClient  http客户端
     * @param httpRequest httpRequest
     * @return 返回处理结果
     */
    public static CloseableHttpResponse execute(CloseableHttpClient httpClient, HttpRequest httpRequest) throws IOException {
        String url = httpRequest.getUrl();

        // 创建请求对象
        HttpRequestBase httpRequestBase = httpRequest.getMethod().createHttpRequest(url);

        // 设置header信息
        httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
        Map<String, String> headers = httpRequest.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpRequestBase.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // 配置请求的设置
        RequestConfig requestConfig = httpRequest.getConfig();
        if (requestConfig != null) {
            httpRequestBase.setConfig(requestConfig);
        }

        // 判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
        if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(httpRequestBase.getClass())) {
            setHttpEntity((HttpEntityEnclosingRequestBase) httpRequestBase, httpRequest);
        } else {
            Map<String, String> params = httpRequest.getParams();
            if (params != null && !params.isEmpty()) {
                // 注意get等不支持设置entity需要更新拼接之后的URL，但是url变量没有更新
                httpRequestBase.setURI(URI.create(buildUrl(url, params, httpRequest.getEncoding())));
            }
        }

        return httpClient.execute(httpRequestBase);
    }

    /**
     * 构建 Url
     *
     * @param url    请求地址
     * @param params 参数
     * @return 拼接之后的地址
     */
    public static String buildUrl(String url, Map<String, String> params) {
        try {
            return buildUrl(url, params, EncodeConstants.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException ignore) {
        }
        return url;
    }


    /**
     * 构建 Url
     *
     * @param url    请求地址
     * @param params 参数
     * @return 拼接之后的地址
     * @throws UnsupportedEncodingException 不支持的编码
     */
    private static String buildUrl(String url, Map<String, String> params, String paramsEncoding) throws UnsupportedEncodingException {
        if (params == null || params.isEmpty()) {
            return url;
        }
        URIBuilder builder;
        try {
            builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = URLEncoder.encode(entry.getKey(), paramsEncoding);
                String value = URLEncoder.encode(entry.getValue(), paramsEncoding);
                builder.setParameter(key, value);
            }
            return builder.build().toString();
        } catch (URISyntaxException e) {
            LoggerFactory.getLogger().debug("Error to build url, please check the arguments.");
        }
        return url;
    }
}
