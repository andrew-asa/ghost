package com.asa.base.ui.jfxsupport;

import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * @author andrew_asa
 * @date 2021/11/21.
 */
public class SplashScreen {

    private static String DEFAULT_IMAGE = "/splash/javafx.png";

    /**
     * 获取动画父容器
     * @return
     */
    public Parent getParent() {
        final ImageView imageView = new ImageView(getClass().getResource(getImagePath()).toExternalForm());
        final ProgressBar splashProgressBar = new ProgressBar();
        splashProgressBar.setPrefWidth(imageView.getImage().getWidth());

        final VBox vbox = new VBox();
        vbox.getChildren().addAll(imageView, splashProgressBar);

        return vbox;
    }

    /**
     * 开机动画是否可见
     */
    public boolean visible() {
        return true;
    }

    /**
     * 获取开机动画图片路径
     *
     * @return "/splash/javafx.png"
     */
    public String getImagePath() {
        return DEFAULT_IMAGE;
    }

}
