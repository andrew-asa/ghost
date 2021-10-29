package com.asa.ghost.weixin.spider.model;

import com.asa.base.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrew_asa
 * @date 2021/3/28.
 */
public class WeixinArticlesInfo {

    private List<WeixinArticle> articles;
    private int articleTotalCount;

    public WeixinArticlesInfo() {

        this.articles = new ArrayList<>();
    }

    public List<WeixinArticle> getArticles() {

        return articles;
    }

    public void setArticles(List<WeixinArticle> articles) {

        this.articles = articles;
    }

    public int getArticleTotalCount() {

        return articleTotalCount;
    }

    public void setArticleTotalCount(int articleTotalCount) {

        this.articleTotalCount = articleTotalCount;
    }

    public void addArticle(WeixinArticle article) {

        makeSureArticlesListIsNoNull();
        articles.add(article);
    }

    public void addArticles(List<WeixinArticle> articles) {

        makeSureArticlesListIsNoNull();
        ListUtils.safeAdd(this.articles,articles);

    }

    private void makeSureArticlesListIsNoNull() {
        if (articles == null) {
            articles = new ArrayList<>();
        }
    }
}
