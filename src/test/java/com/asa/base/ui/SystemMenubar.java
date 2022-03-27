package com.asa.base.ui;

import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.io.ClassPathResource;
import com.asa.ghost.weixin.spider.WeixinSpider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.TimerTask;

/**
 * @author andrew_asa
 * @date 2021/9/26.
 */
public class SystemMenubar extends Application {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Menus");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250, Color.WHITE);

        MenuBar menuBar = new MenuBar();

        //Menu menu = new Menu("File");
        //menu.getItems().add(new MenuItem("New"));
        //menu.getItems().add(new MenuItem("Save"));
        //menu.getItems().add(new SeparatorMenuItem());
        //menu.getItems().add(new MenuItem("Exit"));
        //
        //CustomMenuItem customMenuItem = new CustomMenuItem(new Slider());
        //customMenuItem.setHideOnClick(false);
        //menu.getItems().add(customMenuItem);
        //
        //menuBar.getMenus().add(menu);
        //
        //menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        //try {
        //    initTray(primaryStage,"test");
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        addAppToTray();
        root.getChildren().add(menuBar);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void initTray(Stage stage,String appName) throws Exception {

        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            BufferedImage image = ImageIO.read(new ClassPathResource("com/asa/ghost/weixin/spider/img/Jarvis.png").getInputStream());
            SystemTray tray = SystemTray.getSystemTray();

            trayIcon = new TrayIcon(image, appName);
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip(appName);
            tray.add(trayIcon);
            PopupMenu popupMenu = new PopupMenu();
            java.awt.MenuItem exitItem = new java.awt.MenuItem("退出");
            exitItem.addActionListener(e -> {
                System.exit(0);
            });
            popupMenu.add(exitItem);
            java.awt.MenuItem back = new java.awt.MenuItem("回主页面");
            back.addActionListener(e -> {
                Platform.runLater(() -> {
                    stage.toFront();
                });
            });
            popupMenu.add(back);
            trayIcon.setPopupMenu(popupMenu);

            try {
                TrayIcon[] icons = tray.getTrayIcons();
                if (icons != null) {
                    for (TrayIcon icon : icons) {
                        tray.remove(icon);
                    }
                }
                tray.add(trayIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void addAppToTray() {
        try {
            java.awt.Toolkit.getDefaultToolkit();

            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            java.awt.Image image = ImageIO.read(new ClassPathResource("com/asa/ghost/weixin/spider/img/Jarvis.png").getInputStream());
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.MenuItem openItem = new java.awt.MenuItem("主页");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            java.awt.MenuItem exitItem = new java.awt.MenuItem("退出");
            exitItem.addActionListener(event -> {
                Platform.exit();
                tray.remove(trayIcon);
            });

            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }
}
