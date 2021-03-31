package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.log.LoggerFactory;
import com.asa.utils.ListUtils;
import com.asa.weixin.spider.model.WeixinArticle;
import com.asa.weixin.spider.view.ArticleListPaneView;
import com.asa.weixin.spider.view.WeixinArticleReadView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 * 阅读公众号文章
 */
@FXMLController
public class WeixinArticleReadController implements Initializable {

    @FXML
    private WebView webContainer;

    @FXML
    private Button back;

    @FXML
    private Button forward;

    @FXML
    private Button pageHome;

    @FXML
    private Button snapshot;

    private WebEngine webEngine;

    public enum WeixinArticleReadEvent implements Event<WeixinArticle> {

        /**
         * 请求加载url
         */
        REQUIRE_LOAD,
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoggerFactory.getLogger().debug("WeixinArticleReadController initialize");
        initListener();
        initWebEngine();
        initButtonAction();
    }

    private void initButtonAction() {
        initBackButton();
        initPageHomeButton();
        initSnapshotButton();
        initForwardButton();
    }

    private void initBackButton() {
        back.setOnMouseClicked(e-> back());
        back.setTooltip(new Tooltip("后退"));
    }

    private void initForwardButton() {
        forward.setOnMouseClicked(e-> forward());
        forward.setTooltip(new Tooltip("前进"));
    }


    private void initPageHomeButton() {
        pageHome.setOnMouseClicked(e-> pageHome());
        pageHome.setTooltip(new Tooltip("返回文章列表"));
    }
    private void initSnapshotButton() {
        snapshot.setOnMouseClicked(e->snapshot());
        snapshot.setTooltip(new Tooltip("文章截图"));
    }

    public void snapshot() {
        //Printer.getAllPrinters();
        LoggerFactory.getLogger().debug("snapshot");

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            webEngine.print(job);
            job.endJob();
        }
    }

    public void forward() {
        final WebHistory history=webEngine.getHistory();
        ObservableList<WebHistory.Entry> entryList=history.getEntries();
        int currentIndex=history.getCurrentIndex();
        if (currentIndex + 1 < ListUtils.length(entryList)) {
            history.go(1);
            LoggerFactory.getLogger().debug("forward {}",entryList.get(currentIndex<entryList.size()-1?currentIndex+1:currentIndex).getUrl());
        }
    }

    public void back() {
        final WebHistory history=webEngine.getHistory();
        ObservableList<WebHistory.Entry> entryList=history.getEntries();
        int currentIndex=history.getCurrentIndex();
        if (currentIndex > 0) {
            history.go(-1);
            //LoggerFactory.getLogger().debug("back {}",entryList.get(currentIndex>0?currentIndex-1:currentIndex).getUrl());
        } else {
            pageHome();
        }
    }

    public void pageHome() {
        EventDispatcher.fire(HomePageSubPanelEvent.REQUIRE_INSTALL, ArticleListPaneView.NAME);
    }

    private void initWebEngine() {
        webEngine = webContainer.getEngine();
        //webEngine.getLoadWorker().stateProperty()
        //        .addListener(new ChangeListener<State>() {
        //            @Override
        //            public void changed(ObservableValue ov, State oldState, State newState) {
        //
        //                if (newState == Worker.State.SUCCEEDED) {
        //                    stage.setTitle(webEngine.getLocation());
        //                }
        //
        //            }
        //        });
    }

    private void initListener() {

        EventDispatcher.listen(WeixinArticleReadEvent.REQUIRE_LOAD, new Listener<WeixinArticle>() {

            @Override
            public void on(Event event, WeixinArticle param) {
                EventDispatcher.fire(HomePageSubPanelEvent.REQUIRE_INSTALL, WeixinArticleReadView.NAME);
                webEngine.load(param.getLink());
            }
        });
    }
}
