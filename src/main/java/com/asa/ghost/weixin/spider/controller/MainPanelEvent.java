package com.asa.ghost.weixin.spider.controller;

import com.asa.base.enent.Event;

/**
 * @author andrew_asa
 * @date 2021/2/22.
 *
 */
public enum MainPanelEvent implements Event<String>{
    /**
     * 主板发送安装
     */
    BEFORE_INSTALL,
    /**
     * 安装完成之后
     */
    AFTER_INSTALL,

    /**
     * 主板发送卸载
     */
    UNINSTALL,

    /**
     * 请求安装
     * 主板接收，别的面板请求，像歌词主板
     */
    REQUIRE_INSTALL,

    /**
     * 搜索结构展示
     */
    SEARCH_RESULT_PANE_SHOW,
}
