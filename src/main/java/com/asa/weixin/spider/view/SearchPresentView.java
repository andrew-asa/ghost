package com.asa.weixin.spider.view;

import javafx.scene.Parent;

/**
 * @author andrew_asa
 * @date 2020/11/28.
 * 主表内容
 */
public interface SearchPresentView {

    /**
     * 获取子列表名字
     * 左边框的大分类列表
     * @return
     */
    String getName();

    /**
     * 是否接受选择的名字
     * @param select
     * @return
     */
    boolean accept(String select);

    /**
     * 获取根内容
     * @return
     */
    Parent getView();
}
