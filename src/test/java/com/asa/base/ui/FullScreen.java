package com.asa.base.ui;

import com.asa.base.log.LoggerFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author andrew_asa
 * @date 2021/9/26.
 */
public class FullScreen extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setAlwaysOnTop(true);
        primaryStage.setTitle(this.getClass().getSimpleName());
        primaryStage.setScene(getRootScene());
        primaryStage.show();
        primaryStage.setFullScreen(true);

    }

    public Scene getRootScene() {

        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double width = screenRectangle.getWidth();
        double height = screenRectangle.getHeight();
        LoggerFactory.getLogger().debug("screen width {}, height {}", width, height);
        return new Scene(getContentNode(), width, height);
    }

    public Parent getContentNode() {

        VBox root = new VBox();
        Image full = new Image("com/asa/base/ui/full_screen.png");
        ImageView imageView = new ImageView(full);
        root.getChildren().add(imageView);
        return root;
    }
}
