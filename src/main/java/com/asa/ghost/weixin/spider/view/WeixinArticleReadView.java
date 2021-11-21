package com.asa.ghost.weixin.spider.view;

import com.asa.base.ui.jfxsupport.AbstractFxmlView;
import com.asa.base.ui.jfxsupport.FXMLView;
import com.asa.base.utils.StringUtils;

/**
 * @author andrew_asa
 * @date 2020/11/28.
 * 文章阅读
 */
@FXMLView
public class WeixinArticleReadView extends AbstractFxmlView implements SubViewContent{

    public static final String NAME = "WeixinArticleReadView";



    @Override
    public String getName() {

        return NAME;
    }

    @Override
    public boolean accept(String select) {

        return StringUtils.equalsIgnoreCase(select,NAME);
    }
}
