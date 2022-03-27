package com.asa.base.ui.jfxsupport;

import com.asa.base.ui.jfxsupport.debugger.DebuggerController;
import com.asa.base.utils.io.ClassPathResource;
import com.asa.ghost.weixin.spider.WeixinSpider;
import com.asa.ghost.weixin.spider.view.MainView;
import com.asa.ghost.weixin.spider.view.SplashScreenCustom;
import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.io.IOException;

/**
 * @author andrew_asa
 * @date 2021/11/23.
 */
public class JDebuggerTest extends Application {

    private DebuggerController debugger;

    private Stage stage;

    private Scene scene;

    private StackPane stackPane;

    public void start(final Stage stage) throws Exception {

        this.stage = stage;
        stage.setTitle("JDebuggerTest");
        stackPane = new StackPane();
        HBox root = new HBox();
        stackPane.getChildren().add(root);
        JFXButton button = new JFXButton("test");
        root.getChildren().add(button);

        scene = new Scene(stackPane, 300, 250, Color.WHITE);
        stage.setScene(scene);
        setOnClose(stage);
        initDebugger(stage, scene, stackPane);
        setSystemTray(stage);
        stage.show();
    }

    private void initDebugger(Stage stage, Scene scene, StackPane root) {

        debugger = new DebuggerController();
        debugger.init(stage, scene, root);
    }

    private void setOnClose(Stage stage) {

        stage.setOnCloseRequest(event -> {
            onClose();
        });
    }

    private void onClose() {

        System.exit(0);
        if (tray != null && trayIcon != null) {
            tray.remove(trayIcon);
        }
    }

    java.awt.SystemTray tray;

    java.awt.TrayIcon trayIcon;

    private void setSystemTray(final Stage stage) {

        try {
            java.awt.Toolkit.getDefaultToolkit();
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                return;
            }
            tray = java.awt.SystemTray.getSystemTray();
            java.awt.Image image = ImageIO.read(new ClassPathResource("com/asa/ghost/weixin/spider/img/Jarvis.png").getInputStream());
            trayIcon = new java.awt.TrayIcon(image);
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));
            java.awt.MenuItem openItem = new java.awt.MenuItem("主页");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);
            java.awt.MenuItem exitItem = new java.awt.MenuItem("退出");
            exitItem.addActionListener(event -> {
                onClose();
            });
            java.awt.MenuItem debuggerM = debugger.createDebuggerMenu("调试", "退出调试");
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            popup.addSeparator();
            popup.add(debuggerM);
            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    private void showStage() {

        if (GUIState.getStage() != null) {
            GUIState.getStage().show();
            GUIState.getStage().toFront();
        }
    }
}