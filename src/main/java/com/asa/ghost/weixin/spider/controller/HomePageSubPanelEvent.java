package com.asa.ghost.weixin.spider.controller;

import com.asa.base.enent.Event;

/**
 * @author andrew_asa
 * @date 2021/2/22.
 *
 */
public enum HomePageSubPanelEvent implements Event<String>{
    /**
     * 主板发送安装
     */
    INSTALL,

    /**
     * 主板发送卸载
     */
    UNINSTALL,

    /**
     * 请求安装
     */
    REQUIRE_INSTALL,
}
