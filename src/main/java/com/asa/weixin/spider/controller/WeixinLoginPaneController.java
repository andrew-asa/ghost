package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.log.LoggerFactory;
import com.asa.weixin.spider.service.WeixinLoginService;
import com.asa.weixin.spider.view.WeixinLoginPaneView;
import com.asa.weixin.spider.view.HomePagePaneView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@FXMLController
public class WeixinLoginPaneController implements Initializable {

    @FXML
    private ImageView qrcode;

    @FXML
    private Label loginTip;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoggerFactory.getLogger().debug(this.getClass(),"LoginPaneController initialize");
        initAction();
    }


    private void initAction() {
        initQrcode();
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

    public void initQrcode() {

        EventDispatcher.listen(WeixinLoginService.BrowserServiceEvent.CREATE_QRCODE, new Listener<Object>() {

            @Override
            public void on(Event event, Object param) {

                if (param != null) {
                    EventDispatcher.fire(MainPanelEvent.REQUIRE_INSTALL, WeixinLoginPaneView.NAME);
                    loadQrcode((byte[]) param);
                }
            }
        });
    }


    public void loadQrcode(byte[] bytes) {

        if (qrcode.getImage() == null) {

            if (bytes != null) {
                qrcode.setImage(new Image(new ByteArrayInputStream(bytes)));
            }
        }
    }
}
