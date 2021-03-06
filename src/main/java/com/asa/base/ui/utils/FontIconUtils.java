package com.asa.base.ui.utils;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author andrew_asa
 * @date 2021/10/8.
 */
public class FontIconUtils {

    public static final int DEFAULT_ICON_SIZE = 16;

    public static Button createIconButton(Ikon des) {

        return createIconButton(des, DEFAULT_ICON_SIZE, Color.BLACK);
    }

    public static Button createIconButton(Ikon des, int iconSize) {

        return createIconButton(des, iconSize, Color.BLACK);
    }

    public static Button createIconButton(Ikon des, Color color) {

        return createIconButton(des, DEFAULT_ICON_SIZE, color);
    }

    public static Button createIconButton(Ikon des, int iconSize, Color color) {

        JFXButton button = new JFXButton();
        FontIcon fontIcon = FontIcon.of(des, iconSize, color);
        button.setGraphic(fontIcon);
        return button;
    }
}
