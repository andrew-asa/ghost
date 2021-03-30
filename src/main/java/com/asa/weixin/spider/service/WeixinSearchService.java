package com.asa.weixin.spider.service;

import com.asa.utils.ListUtils;
import com.asa.utils.MapBuilder;
import com.asa.utils.MapUtils;
import com.asa.weixin.spider.model.WeixinArticle;
import com.asa.weixin.spider.model.WeixinArticlesInfo;
import com.asa.weixin.spider.model.WeixinPublicAccount;
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
     * 搜索公众号文章
     * @param account
     * @return
     */
    public WeixinArticlesInfo searchArticle(WeixinPublicAccount account) {

        return searchArticle(account,0,5);
    }

    /**
     * 搜索公众号文章
     * @param account
     * @param begin
     * @param count
     * @return
     */
    public WeixinArticlesInfo searchArticle(WeixinPublicAccount account, int begin, int count) {

        return searchArticle(loginService.getToken(),
                             loginService.getCookieString(),
                             account,begin,count);
    }


    /**
     * 搜索公众号文章
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
                                            int begin,
                                            int count) {

        return searchArticle(token,cookie,account.getFakeId(),
                             "",
                             begin,count);
    }

    public WeixinArticlesInfo searchArticle(String token, String cookie,String fakeId,
                                            String query,
                                            int begin,int count) {
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

    public WeixinArticlesInfo parseSearchArticleResult(Map body){
        WeixinArticlesInfo ret = new WeixinArticlesInfo();
        if (body != null) {
            int msgCount =(int)MapUtils.get(body, "app_msg_cnt");
            ret.setArticleTotalCount(msgCount);
            List<Map<String,Object>> list = (List<Map<String, Object>>) MapUtils.get(body, "app_msg_list");
            if (ListUtils.isNotEmpty(list)) {
                list.forEach(item->{
                    WeixinArticle article = new WeixinArticle();
                    article.setAid((String) item.get("aid"));
                    article.setCover((String) item.get("cover"));
                    article.setTitle((String) item.get("title"));
                    article.setLink((String) item.get("link"));
                    article.setDigest((String) item.get("digest"));
                    article.setUpdate_time((Integer) item.get("update_time"));
                    article.setCreate_time((Integer) item.get("create_time"));
                    article.setCopyright_type((int) item.get("copyright_type"));
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


        return searchPublicAccount(keyword,loginService.getToken(),loginService.getCookieString());
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
                                                         int count){
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

    public List<WeixinPublicAccount> parseSearchPublicAccountResult(Map body){
        List<WeixinPublicAccount> ret = new ArrayList<>();
        if (MapUtils.isNotEmptyMap(body)) {
            List<Map<String,Object>> list = (List<Map<String,Object>>) MapUtils.get(body, "list");
            if (ListUtils.isNotEmpty(list)) {
                list.forEach(item ->{
                    WeixinPublicAccount account = new WeixinPublicAccount();
                    account.setAlias((String) MapUtils.get(item, "alias"));
                    account.setFakeId((String)MapUtils.get(item, "fakeid"));
                    account.setNickName((String)MapUtils.get(item, "nickname"));
                    account.setRoundHeadImg((String)MapUtils.get(item, "round_head_img"));
                    account.setServiceType(item.get("service_type").toString());
                    account.setSignature((String)MapUtils.get(item, "signature"));
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
