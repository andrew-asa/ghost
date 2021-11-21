package com.asa.ghost.weixin.spider.view;

import com.asa.base.utils.StringUtils;
import com.asa.base.ui.jfxsupport.AbstractFxmlView;
import com.asa.base.ui.jfxsupport.FXMLView;

/**
 * @author andrew_asa
 * @date 2020/11/28.
 * 文章列表
 */
@FXMLView
public class ArticleListPaneView extends AbstractFxmlView implements SubViewContent{

    public static final String NAME = "articleListPane";

    @Override
    public String getName() {

        return NAME;
    }

    @Override
    public boolean accept(String select) {

        return StringUtils.equalsIgnoreCase(select,NAME);
    }
}
