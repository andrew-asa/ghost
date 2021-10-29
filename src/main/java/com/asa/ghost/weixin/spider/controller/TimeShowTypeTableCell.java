package com.asa.ghost.weixin.spider.controller;

import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableCell;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author andrew_asa
 * @date 2021/3/27.
 */
public class TimeShowTypeTableCell<S, T> extends TableCell<S, T> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void init() {
        setTextOverrun(OverrunStyle.CLIP);
    }

    public TimeShowTypeTableCell() {

        setTextOverrun(OverrunStyle.CLIP);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            long t = (Long) item;
            setText(dateFormat.format(new Date(t*1000)));
        }
    }
}
