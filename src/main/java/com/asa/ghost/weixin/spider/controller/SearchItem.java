package com.asa.ghost.weixin.spider.controller;

import java.awt.print.PrinterGraphics;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author andrew_asa
 * @date 2021/3/27.
 */
public class SearchItem {

    /*
     *引擎
     */
    private String tag;

    /**
     * 关键词
     */
    private String keyword;

    public SearchItem(String tag, String keyword) {

        this.tag = tag;
        this.keyword = keyword;
    }

    public String getTag() {

        return tag;
    }

    public void setTag(String tag) {

        this.tag = tag;
    }

    public String getKeyword() {

        return keyword;
    }

    public void setKeyword(String keyword) {

        this.keyword = keyword;
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", SearchItem.class.getSimpleName() + "[", "]")
                .add("tag='" + tag + "'")
                .add("keyword='" + keyword + "'")
                .toString();
    }
}
