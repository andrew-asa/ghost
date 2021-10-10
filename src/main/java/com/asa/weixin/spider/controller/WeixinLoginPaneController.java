package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.browser.Browser;
import com.asa.browser.base.JBrowserDebugger;
import com.asa.browser.widget.degger.element.WebElement;
import com.asa.browser.widget.degger.selector.By;
import com.asa.base.log.LoggerFactory;
import com.asa.weixin.spider.service.WeixinLoginService;
import com.asa.weixin.spider.view.HomePagePaneView;
import com.asa.weixin.spider.view.WeixinLoginPaneView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@FXMLController
public class WeixinLoginPaneController implements Initializable {

    @FXML
    private BorderPane mainWindow;

    private Browser browser;

    @Autowired
    private WeixinLoginService loginService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoggerFactory.getLogger().debug(this.getClass(), "LoginPaneController initialize");
        initAction();
    }


    private void initAction() {
        initLoginBrowser();
        initLoginAction();
    }

    public void initLoginAction() {

        EventDispatcher.listen(WeixinLoginService.BrowserServiceEvent.LOGIN_SUCCESS, new Listener<Object>() {

            @Override
            public void on(Event event, Object param) {

                EventDispatcher.fire(MainPanelEvent.REQUIRE_INSTALL, HomePagePaneView.NAME);
            }
        });
    }

    private void initLoginBrowser() {

        browser = new Browser();
        mainWindow.setCenter(browser);
        EventDispatcher.listen(WeixinLoginService.BrowserServiceEvent.RE_LOGIN, new Listener<Object>() {

            @Override
            public void on(Event event, Object param) {

                EventDispatcher.fire(MainPanelEvent.REQUIRE_INSTALL, WeixinLoginPaneView.NAME);
                Platform.runLater(()->reLogin());
            }
        });
    }

    private void reLogin() {
        browser.load(WeixinLoginService.LOGIN_URL);
        JBrowserDebugger debugger = browser.getDebugger();
        WebElement element = debugger.findElement(By.className("weui-desktop-account__info"));
        element.waitExistUntil(300, (back)-> {
            if (back) {
                String cookie = debugger.getCookie();
                loginService.savaCookie(cookie,browser.getLocation());
                EventDispatcher.fire(WeixinLoginService.BrowserServiceEvent.LOGIN_SUCCESS, null);
            }else {
                // 等
                LoggerFactory.getLogger().debug("reLogin again");
                reLogin();
            }
        });
    }
}
