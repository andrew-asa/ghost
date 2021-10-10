package com.asa.weixin.spider.service;

import com.asa.base.utils.ListUtils;
import com.asa.base.utils.MapBuilder;
import com.asa.base.utils.MapUtils;
import com.asa.base.utils.StringUtils;
import com.asa.weixin.spider.model.WeixinArticle;
import com.asa.weixin.spider.model.WeixinArticlesInfo;
import com.asa.weixin.spider.model.WeixinPublicAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/3/27.
 */
@Component
public class WeixinSearchService {

    @Autowired
    private WeixinLoginService loginService;

    /**
     * 公众号查询
     */
    public static final String ACCOUNT_SEARCH_URL = "https://mp.weixin.qq.com/cgi-bin/searchbiz?action={action}&begin={begin}&count={count}&query={query}&token={token}&lang={lang}&f={f}&ajax={ajax}";

    /**
     * 公众号文章url查询
     */
    public static final String ARTICLE_SEARCH_URL = "https://mp.weixin.qq.com/cgi-bin/appmsg?action={action}&begin={begin}&count={count}&fakeid={fakeid}&type={type}&query={query}&token={token}&lang={lang}&f={f}&ajax={ajax}";

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 每页显示多少条数据
     */
    public int pageCount = 10;

    public int getPageCount() {

        return pageCount;
    }

    public void setPageCount(int pageCount) {

        this.pageCount = pageCount;
    }

    /**
     * 获取第 n 页文章
     *
     * @param weixinPublicAccount
     * @param keyword
     * @param page
     * @return
     */
    public WeixinArticlesInfo searchPageArticles(WeixinPublicAccount weixinPublicAccount, String keyword, int page) {

        int startIndex = (page - 1) * pageCount;
        int lastIndex = startIndex + pageCount - 1;
        WeixinArticlesInfo weixinArticlesInfo = searchArticle(weixinPublicAccount, keyword, startIndex, pageCount);
        int totalCount = weixinArticlesInfo.getArticleTotalCount();
        int resultCount = ListUtils.length(weixinArticlesInfo.getArticles());
        int resultIndex = startIndex + resultCount - 1;
        // 一次中
        if (lastIndex == resultIndex) {
            return weixinArticlesInfo;
        }
        // 直接去最小，确保不越界
        lastIndex = Math.min(lastIndex, totalCount - 1);
        // 多了
        if (lastIndex < resultIndex) {
            // 截取一页直接返回
            weixinArticlesInfo.setArticles(weixinArticlesInfo.getArticles().subList(0, pageCount));
        }
        // 少了,只再试一次，其它不管
        if (lastIndex > resultIndex) {
            // 坑爹的腾讯能确保有5条记录，但是无法确保每次一样 ++++
            if (resultCount >= 5) {
                resultIndex = resultIndex - (resultCount - 5);
                weixinArticlesInfo.setArticles(weixinArticlesInfo.getArticles().subList(0, 5));
            }
            int les = lastIndex - resultIndex;
            startIndex = startIndex + 5;
            WeixinArticlesInfo ex = searchArticle(weixinPublicAccount, startIndex, pageCount);
            List<WeixinArticle> ar = ex.getArticles();
            int exLent = ListUtils.length(ar);
            if (exLent > 0) {
                int ti = Math.min(les, exLent);
                weixinArticlesInfo.addArticles(ar.subList(0, ti));
            }
        }
        //try {
        //    System.out.println(new ObjectMapper().writeValueAsString(weixinArticlesInfo));
        //} catch (Exception e) {
        //
        //}
        return weixinArticlesInfo;
    }


    /**
     * 搜索公众号文章
     *
     * @param account
     * @return
     */
    public WeixinArticlesInfo searchArticle(WeixinPublicAccount account) {

        return searchArticle(account, 0, 5);
    }

    /**
     * 搜索公众号文章
     *
     * @param account
     * @param begin
     * @param count
     * @return
     */
    public WeixinArticlesInfo searchArticle(WeixinPublicAccount account,
                                            int begin, int count) {

        return searchArticle(account, StringUtils.EMPTY, begin, count);
    }

    public WeixinArticlesInfo searchArticle(WeixinPublicAccount account,
                                            String query,
                                            int begin, int count) {

        return searchArticle(loginService.getToken(),
                             loginService.getCookieString(),
                             account, query,
                             begin, count);
    }


    /**
     * 搜索公众号文章
     *
     * @param token
     * @param cookie
     * @param account
     * @param begin
     * @param count
     * @return
     */
    public WeixinArticlesInfo searchArticle(String token,
                                            String cookie,
                                            WeixinPublicAccount account,
                                            String query,
                                            int begin,
                                            int count) {

        return searchArticle(token, cookie, account.getFakeId(),
                             query,
                             begin, count);
    }

    public WeixinArticlesInfo searchArticle(String token, String cookie,
                                            String fakeId,
                                            String query,
                                            int begin, int count) {

        Map<String, Object> params = new MapBuilder()
                .add("action", "list_ex")
                .add("begin", begin)
                .add("count", count)
                .add("fakeid", fakeId)
                .add("type", 9)
                .add("query", query)
                .add("token", token)
                .add("lang", "zh_CN")
                .add("f", "json")
                .add("ajax", "1")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
        headers.set("cookie", cookie);
        // 注意几个请求参数
        HttpEntity<Map> res = restTemplate
                .exchange(ARTICLE_SEARCH_URL, HttpMethod.GET, new HttpEntity<Map>(null, headers),
                          Map.class, params);
        Map body = res.getBody();
        return parseSearchArticleResult(body);
    }

    /**
     * session 过期了，重新获取
     */
    private void newLogin() {

        loginService.newLogin();
    }

    private boolean invalidSession(Map body) {

        if (MapUtils.containsKey(body, "base_resp")) {
            Object baseResp = MapUtils.get(body, "base_resp");
            if (baseResp instanceof Map) {
                Object v = MapUtils.get((Map) baseResp, "ret");
                if (v instanceof Integer && (Integer) v == 200003) {
                    return true;
                }
            }
        }
        return false;
    }

    public WeixinArticlesInfo parseSearchArticleResult(Map body) {

        WeixinArticlesInfo ret = new WeixinArticlesInfo();
        if (body != null) {
            if (invalidSession(body)) {
                newLogin();
                return ret;
            }
            int msgCount = (int) MapUtils.get(body, "app_msg_cnt");
            ret.setArticleTotalCount(msgCount);
            List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.get(body, "app_msg_list");
            if (ListUtils.isNotEmpty(list)) {
                list.forEach(item -> {
                    WeixinArticle article = new WeixinArticle();
                    article.setAid((String) item.get("aid"));
                    article.setCover((String) item.get("cover"));
                    article.setTitle((String) item.get("title"));
                    article.setLink((String) item.get("link"));
                    article.setDigest((String) item.get("digest"));
                    article.setUpdate_time(MapUtils.getInteger(item, "update_time"));
                    article.setUpdate_time(MapUtils.getInteger(item, "update_time"));
                    // 带有关键字搜索的时候这个字段不一定有
                    article.setCopyright_type(MapUtils.getInteger(item, "copyright_type"));
                    ret.addArticle(article);
                });
            }
        }
        return ret;
    }

    /**
     * 根据关键词搜索公众号
     *
     * @param keyword
     * @return
     */
    public List<WeixinPublicAccount> searchPublicAccount(String keyword) {


        return searchPublicAccount(keyword, loginService.getToken(), loginService.getCookieString());
    }

    public List<WeixinPublicAccount> searchPublicAccount(String keyword,
                                                         String token,
                                                         String cookie) {

        return searchPublicAccount(keyword, token, cookie, 0, 5);

    }

    public List<WeixinPublicAccount> searchPublicAccount(String keyword,
                                                         String token,
                                                         String cookie,
                                                         int begin,
                                                         int count) {

        Map<String, Object> params = new MapBuilder()
                .add("action", "search_biz")
                .add("begin", begin)
                .add("count", count)
                .add("query", keyword)
                .add("token", token)
                .add("lang", "zh_CN")
                .add("f", "json")
                .add("ajax", "1")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
        headers.set("cookie", cookie);
        // 注意几个请求参数
        HttpEntity<Map> res = restTemplate
                .exchange(ACCOUNT_SEARCH_URL, HttpMethod.GET, new HttpEntity<Map>(null, headers),
                          Map.class, params);
        Map body = res.getBody();
        return parseSearchPublicAccountResult(body);
    }

    public List<WeixinPublicAccount> parseSearchPublicAccountResult(Map body) {

        List<WeixinPublicAccount> ret = new ArrayList<>();
        if (MapUtils.isNotEmptyMap(body)) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) MapUtils.get(body, "list");
            if (ListUtils.isNotEmpty(list)) {
                list.forEach(item -> {
                    WeixinPublicAccount account = new WeixinPublicAccount();
                    account.setAlias((String) MapUtils.get(item, "alias"));
                    account.setFakeId((String) MapUtils.get(item, "fakeid"));
                    account.setNickName((String) MapUtils.get(item, "nickname"));
                    account.setRoundHeadImg((String) MapUtils.get(item, "round_head_img"));
                    account.setServiceType(item.get("service_type").toString());
                    account.setSignature((String) MapUtils.get(item, "signature"));
                    ret.add(account);
                });
            }
        }
        return ret;
    }


    public List<WeixinPublicAccount> getCommonUsedAccount() {

        List<WeixinPublicAccount> accountList = new ArrayList<>();
        accountList.add(WeixinPublicAccount.create("政事堂pro2019", "zhengshitang2019", "http://mmbiz.qpic.cn/mmbiz_png/zyEYFkZWMFgesd1TyKysLrtNBZ9lqbsuhVDXJOSRbSjSwjCDYg47UnqKpaS5yTYPAruVbzOs31uf2Bv2Dle2aA/0?wx_fmt=png", "1"));
        accountList.add(WeixinPublicAccount.create("求实处", "", "http://mmbiz.qpic.cn/mmbiz_png/hr6mUjEf2jaa8tz3bBGpZl2HuHBePdpflnvbVsEnkbwVmGG1HZkuU5XSA5WwcuNB6sX4L0dr6b9qy5q52w3GzA/0?wx_fmt=png", "1"));
        return accountList;
    }
}
