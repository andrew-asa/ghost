package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.StringUtils;
import com.asa.weixin.spider.model.WeixinPublicAccount;
import com.asa.weixin.spider.service.WeixinFavorAccountsService;
import com.asa.weixin.spider.service.WeixinSearchService;
import com.asa.weixin.spider.view.FavorAccountsView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 * 收藏号
 */
@FXMLController
public class FavorAccountsController implements Initializable {

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
    private WeixinFavorAccountsService favorAccountsService;

    private ObservableList<WeixinPublicAccount> accounts;

    public enum FavorAccountEvent implements Event<WeixinPublicAccount> {
        /**
         * 置顶
         */
        TO_LIST_TOP,

        /**
         * 置尾
         */
        TO_LIST_BOTTOM,

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoggerFactory.getLogger().debug(this.getClass(),"FavorAccountsController initialize");
        iniTable();
        initListener();
    }

    private void initListener() {

        EventDispatcher.listen(HomePageSubPanelEvent.INSTALL, new Listener<String>() {

            @Override
            public void on(Event event, String param) {

                if (StringUtils.equals(FavorAccountsView.NAME, param)) {
                    showFavorAccount();
                }
            }
        });
        EventDispatcher.listen(FavorAccountEvent.TO_LIST_TOP, new Listener<WeixinPublicAccount>() {

            @Override
            public void on(Event event, WeixinPublicAccount param) {
                accounts.remove(param);
                accounts.add(0, param);
                updateFavourSeqAccount();
            }
        });
        EventDispatcher.listen(FavorAccountEvent.TO_LIST_BOTTOM, new Listener<WeixinPublicAccount>() {

            @Override
            public void on(Event event, WeixinPublicAccount param) {
                accounts.remove(param);
                accounts.add(param);
                updateFavourSeqAccount();
            }
        });
    }

    public void updateFavourSeqAccount() {

        favorAccountsService.updateFavourSeqAccount(accounts);
    }

    public void showFavorAccount() {

        accounts = FXCollections.observableList(favorAccountsService.getAllFavorAccountsBySeq());
        tableView.setItems(accounts);
    }

    private void iniTable() {

        settingTableView();
        settingTableListener();
    }

    private void settingTableListener() {

        tableView.setRowFactory(x -> {
            TableRow<WeixinPublicAccount> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    customRowContextMenu(row);
                } else if (event.getClickCount() == 2) {
                    WeixinPublicAccount account = row.getItem();
                    searchWeixinAccountArticle(account);
                }
            });
            return row;
        });
    }

    private ContextMenu customRowContextMenu(TableRow<WeixinPublicAccount> row) {

        WeixinPublicAccount account = row.getItem();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem toTop = new MenuItem("置顶");
        toTop.setOnAction(e -> {
            accountToTop(account);
        });
        MenuItem toBottom = new MenuItem("置底");
        toBottom.setOnAction(e -> {
            accountToBottom(account);
        });
        contextMenu.getItems().addAll(toTop, toBottom);
        row.setContextMenu(contextMenu);
        contextMenu.show(row, 0, 0);
        return contextMenu;
    }


    private void accountToTop(WeixinPublicAccount account) {

        LoggerFactory.getLogger().debug(this.getClass(),"to top{}", account);

        EventDispatcher.asyncFire(FavorAccountEvent.TO_LIST_TOP,account);
    }

    private void accountToBottom(WeixinPublicAccount account) {

        LoggerFactory.getLogger().debug(this.getClass(),"to bottom{}", account);

        EventDispatcher.asyncFire(FavorAccountEvent.TO_LIST_BOTTOM,account);
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
}
