package com.asa.base.ui.control;

import com.asa.base.utils.StringUtils;
import javafx.scene.control.Control;

/**
 * @author andrew_asa
 * @date 2021/11/23.
 */
public class BaseJControl extends Control {

    private String stylesheet;

    public BaseJControl() {

    }

    /**
     * 获取代理的样式文件
     * @param clazz
     * @param fileName
     * @return
     */
    protected final String getUserAgentStylesheet(Class<?> clazz,
                                                  String fileName) {

        if (stylesheet == null && clazz != null && StringUtils.isNotEmpty(fileName)) {
            stylesheet = clazz.getResource(fileName).toExternalForm();
        }
        return stylesheet;
    }
}
