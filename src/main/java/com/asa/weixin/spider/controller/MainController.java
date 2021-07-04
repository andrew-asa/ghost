package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;

import com.asa.log.LoggerFactory;
import com.asa.utils.StringUtils;
import com.asa.weixin.spider.Spider;
import com.asa.weixin.spider.view.HomePagePaneView;
import com.asa.weixin.spider.view.MainViewContent;

import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@FXMLController
public class MainController implements Initializable {

    @FXML
    private BorderPane mainWindow;

    @Autowired
    private List<MainViewContent> subViews;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTray();
        installListener();
        preLoadSubViews();
        installMainPanel();
    }

    private void setTray() {
        try {
            BufferedImage image = ImageIO.read(new ClassPathResource("com/asa/weixin/spider/img/Jarvis.png").getInputStream());
            TrayIcon trayIcon = initTray("jarvis", image);
            Spider.getStage().setOnCloseRequest(event -> {
                try {
                    if (SystemTray.isSupported())
                        if (trayIcon != null)
                            SystemTray.getSystemTray().remove(trayIcon);
                } catch (Exception ignored) {
                }
                System.exit(0);
            });
        } catch (Exception e) {

        }

    }

    private TrayIcon initTray(String appName, BufferedImage bufferedImage) throws Exception {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            //BufferedImage bufferedImage = SwingFXUtils.fromFXImage(logoImage, null);
            PopupMenu popupMenu = new PopupMenu();
            java.awt.MenuItem exitItem = new java.awt.MenuItem("退出");
            exitItem.addActionListener(e -> { System.exit(0);});
            popupMenu.add(exitItem);
            java.awt.MenuItem back = new java.awt.MenuItem("回主页面");
            back.addActionListener(e -> {
                Platform.runLater(() -> {
                    Stage stage = Spider.getStage();
                    if(!Spider.getStage().isShowing()){
                        stage.show();
                    }
                    stage.toFront();
                });
            });
            popupMenu.add(back);
            if (bufferedImage != null) {
                trayIcon = new TrayIcon(bufferedImage, appName);
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip(appName);
                trayIcon.setPopupMenu(popupMenu);
                tray.add(trayIcon);
            }
        }
        return trayIcon;
    }

    private void preLoadSubViews() {

        subViews.forEach(e -> e.getView());
    }

    private void installListener() {


        EventDispatcher.listen(MainPanelEvent.REQUIRE_INSTALL, new Listener<String>() {

            @Override
            public void on(Event event, String param) {

                loadSubView(param);
            }
        });

    }

    private void installMainPanel() {
        loadSubView(HomePagePaneView.NAME);
    }

    public MainViewContent getSubView(String viewName) {

        Optional<MainViewContent> sbv = subViews.stream()
                .filter((sb) -> {
                    return sb.accept(viewName);
                })
                .findFirst();
        return sbv.isPresent() ? sbv.get() : null;
    }

    private String lastView = StringUtils.EMPTY;

    public void loadSubView(String viewName) {

        if (StringUtils.equals(viewName, lastView)) {
            return;
        }
        MainViewContent subViewContent = getSubView(viewName);
        if (subViewContent == null) {
            return;
        }
        String tl = lastView;
        lastView = viewName;
        Node view = subViewContent.getView();
        Platform.runLater(() -> {
            EventDispatcher.fire(MainPanelEvent.UNINSTALL, tl);
            LoggerFactory.getLogger().debug(this.getClass(),"uninstall {}",tl);
            EventDispatcher.fire(MainPanelEvent.BEFORE_INSTALL, viewName);
            mainWindow.setCenter(view);
            LoggerFactory.getLogger().debug(this.getClass(),"install {}",viewName);
            Spider.getStage().setHeight(((Pane) view).getPrefHeight());
            Spider.getStage().setWidth(((Pane) view).getPrefWidth());
            ((Pane) view).prefWidthProperty().bind(mainWindow.widthProperty());
            ((Pane) view).prefHeightProperty().bind(mainWindow.heightProperty());
            EventDispatcher.fire(MainPanelEvent.AFTER_INSTALL, viewName);
        });
    }
}
