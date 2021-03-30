package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;

/**
 * @author andrew_asa
 * @date 2021/3/27.
 * 搜索事件
 */
public enum SearchEvent implements Event<SearchItem> {

    /**
     * 开始搜索
     */
    START_SEARCH,
    /**
     * 结束搜索
     */
    STOP_SEARCH,
}
