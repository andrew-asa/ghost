package com.asa.weixin.spider.controller;

import com.asa.base.utils.StringUtils;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableCell;

/**
 * @author andrew_asa
 * @date 2021/3/27.
 */
public class AliasTableCell<S, T> extends TableCell<S, T> {

    public void init() {
        setTextOverrun(OverrunStyle.CLIP);
    }

    public AliasTableCell() {

        setTextOverrun(OverrunStyle.CLIP);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            String s = "";
            if (item == null || StringUtils.isEmpty(item.toString())) {
                s = "未设置";
            } else {
                s = item.toString();
            }
            setText(s);
        }
    }
}
