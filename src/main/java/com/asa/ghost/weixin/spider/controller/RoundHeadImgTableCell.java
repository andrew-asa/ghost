package com.asa.ghost.weixin.spider.controller;

import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.StringUtils;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * @author andrew_asa
 * @date 2021/3/27.
 */
public class RoundHeadImgTableCell<S, T> extends TableCell<S, T> {


    @Override
    protected void updateItem(T item, boolean empty) {

        super.updateItem(item, empty);
        if (empty || item == null || StringUtils.isEmpty((String) item)) {
            setText(null);
            setGraphic(null);
        } else {
            String url = item.toString();
            try {
                ImageView imageView = new ImageView(new Image(url));
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                setGraphic(imageView);
            } catch (Exception e) {
                LoggerFactory.getLogger().debug(e,"error fetch image {}", item);
            }
        }
    }
}
