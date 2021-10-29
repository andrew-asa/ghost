package com.asa.ghost.weixin.spider.view;

import com.asa.base.enent.Event;
import com.asa.base.utils.StringUtils;
import com.asa.ghost.weixin.spider.controller.SearchItem;
import com.asa.ghost.weixin.spider.model.WeixinArticle;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;

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
