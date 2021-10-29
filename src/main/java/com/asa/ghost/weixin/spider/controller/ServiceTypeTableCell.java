package com.asa.ghost.weixin.spider.controller;

import com.asa.ghost.weixin.spider.model.WeixinPublicAccount;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableCell;

/**
 * @author andrew_asa
 * @date 2021/3/27.
 */
public class ServiceTypeTableCell<S, T> extends TableCell<S, T> {

    public void init() {
        setTextOverrun(OverrunStyle.CLIP);
    }

    public ServiceTypeTableCell() {

        setTextOverrun(OverrunStyle.CLIP);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(WeixinPublicAccount.ServiceType.showName(item.toString()));
        }
    }
}
