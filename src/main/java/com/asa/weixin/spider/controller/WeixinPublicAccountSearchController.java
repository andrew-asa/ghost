package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.log.LoggerFactory;
import com.asa.weixin.spider.model.WeixinPublicAccount;
import com.asa.weixin.spider.service.WeixinSearchService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@FXMLController
public class WeixinPublicAccountSearchController implements Initializable {

    @FXML
    private BorderPane publicAccount;

    @FXML
    private TableView<WeixinPublicAccount> tableView;

    @FXML
    private TableColumn<WeixinPublicAccount, String> roundHeadImg;

    @FXML
    private TableColumn<WeixinPublicAccount, String> nickName;

    @FXML
    private TableColumn<WeixinPublicAccount, String> alias;

    @Autowired
    private WeixinSearchService searchService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoggerFactory.getLogger().debug("PublicAccountController initialize");
        iniTable();
        //showCommonUsedAccount();
        initListener();
    }

    private void initListener() {

        EventDispatcher.listen(MainPanelEvent.SEARCH_RESULT_PANE_SHOW, new Listener<String>() {

            @Override
            public void on(Event event, String param) {

                tableView.setItems(FXCollections.emptyObservableList());
            }
        });

        EventDispatcher.listen(SearchEvent.START_SEARCH, new Listener<SearchItem>() {

            @Override
            public void on(Event event, SearchItem param) {

                LoggerFactory.getLogger().debug("start search {}", param);
                List<WeixinPublicAccount> ret = searchService.searchPublicAccount(param.getKeyword());
                showCommonUsedAccount(ret);
            }
        });

        EventDispatcher.listen(SearchEvent.STOP_SEARCH, new Listener<SearchItem>() {

            @Override
            public void on(Event event, SearchItem param) {

            }
        });
    }

    private void iniTable() {

        settingTableView();
        settingTableListener();
    }

    private void settingTableListener() {

        tableView.setRowFactory(x -> {
            TableRow<WeixinPublicAccount> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                WeixinPublicAccount account = row.getItem();
                searchWeixinAccountArticle(account);
            });
            return row ;
        });
    }

    public void searchWeixinAccountArticle(WeixinPublicAccount account) {

        EventDispatcher.asyncFire(ArticleListPaneController.ArticleListEvent.REQUIRE_SHOW_ACCOUNT, account);
    }
    private void settingTableView() {

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        roundHeadImg.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        nickName.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
        alias.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));

        roundHeadImg.setCellFactory(x -> new RoundHeadImgTableCell<>());
        alias.setCellFactory(x -> new AliasTableCell<>());

        roundHeadImg.setCellValueFactory(new PropertyValueFactory<>("roundHeadImg"));
        nickName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        alias.setCellValueFactory(new PropertyValueFactory<>("alias"));
    }

    private void showCommonUsedAccount(List<WeixinPublicAccount> accounts) {

        tableView.setItems(FXCollections.observableArrayList(accounts));
    }
}
