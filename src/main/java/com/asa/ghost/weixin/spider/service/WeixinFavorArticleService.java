package com.asa.ghost.weixin.spider.service;

import com.asa.base.log.LoggerFactory;
import com.asa.ghost.weixin.spider.model.WeixinArticle;
import com.asa.ghost.weixin.spider.model.db.WeixinBookMarksDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrew_asa
 * @date 2021/3/29.
 */
@Component
public class WeixinFavorArticleService {

    @Autowired
    private WeixinBookMarksDao bookMarksDao;

    public WeixinFavorArticleService() {

    }

    public static final String ROOT = "/";

    public List<String> getFolders() {

        List<String> folders = new ArrayList<>();
        folders.add(ROOT);
        return folders;
    }

    /**
     * 生成目录
     */
    public void createBookMarkDir() {

    }


    /**
     *
     * @param url
     * @param title
     * @param folder
     */
    public void addToFavor(String url,String folder,String title) {

        if (url != null) {
            LoggerFactory.getLogger().debug(this.getClass(),"add to favor {},{},{}",folder,title,url);
        }
    }

    /**
     * 移除收藏
     */
    public void removeFavor(String url) {

    }

}
