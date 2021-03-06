package com.asa.ghost.weixin.spider.view;

import com.asa.base.utils.StringUtils;
import com.asa.base.ui.jfxsupport.AbstractFxmlView;
import com.asa.base.ui.jfxsupport.FXMLView;

/**
 * @author andrew_asa
 * @date 2020/11/28.
 * 微信公众号搜索列表
 */
@FXMLView
public class WeixinPublicAccountSearchView extends AbstractFxmlView implements SearchPresentView{

    public static final String NAME = "WeixinPublicAccountSearchView";

    @Override
    public String getName() {

        return NAME;
    }

    @Override
    public boolean accept(String select) {

        return StringUtils.equalsIgnoreCase(select, NAME);
    }
}
