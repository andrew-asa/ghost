package com.asa.ghost.weixin.spider.view;

import com.asa.base.utils.StringUtils;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@FXMLView
public class WeixinLoginPaneView extends AbstractFxmlView implements MainViewContent {

    public static final String NAME = "LoginPan";

    @Override
    public String getName() {

        return NAME;
    }

    @Override
    public boolean accept(String select) {

        return StringUtils.equalsIgnoreCase(select, NAME);
    }
}
