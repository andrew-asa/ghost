package com.asa.ghost.weixin.spider.view;

import com.asa.base.utils.StringUtils;
import com.asa.base.ui.jfxsupport.AbstractFxmlView;
import com.asa.base.ui.jfxsupport.FXMLView;

/**
 * @author andrew_asa
 * @date 2020/11/26.
 */
@FXMLView
public class HomePagePaneView extends AbstractFxmlView implements MainViewContent{

    public static final String NAME = "HomePagePaneView";

    @Override
    public String getName() {

        return NAME;
    }

    @Override
    public boolean accept(String select) {

        return StringUtils.equalsIgnoreCase(select, NAME);
    }
}

