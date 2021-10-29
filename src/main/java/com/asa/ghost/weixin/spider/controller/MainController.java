package com.asa.ghost.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;

import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.StringUtils;
import com.asa.ghost.weixin.spider.Spider;
import com.asa.ghost.weixin.spider.view.HomePagePaneView;
import com.asa.ghost.weixin.spider.view.MainViewContent;

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
        installListener();
        preLoadSubViews();
        installMainPanel();
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
