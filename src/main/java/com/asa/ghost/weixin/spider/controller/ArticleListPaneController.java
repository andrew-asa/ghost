package com.asa.ghost.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.ListUtils;
import com.asa.base.utils.StringUtils;
import com.asa.base.utils.math.NumberUtils;
import com.asa.ghost.weixin.spider.model.WeixinArticle;
import com.asa.ghost.weixin.spider.model.WeixinArticlesInfo;
import com.asa.ghost.weixin.spider.model.WeixinPublicAccount;
import com.asa.ghost.weixin.spider.service.WeixinFavorAccountsService;
import com.asa.ghost.weixin.spider.service.WeixinSearchService;
import com.asa.base.ui.component.Toast;
import com.asa.ghost.weixin.spider.view.ArticleListPaneView;
import com.asa.ghost.weixin.spider.view.FavorAccountsView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@FXMLController
public class ArticleListPaneController implements Initializable {

    @FXML
    private BorderPane publicAccount;

    @FXML
    private TableView<WeixinArticle> tableView;

    @FXML
    private TableColumn<WeixinPublicAccount, String> title;

    @FXML
    private TableColumn<WeixinPublicAccount, Long> updateTime;

    @FXML
    private ImageView headImage;

    @FXML
    private Label totalNumber;

    @FXML
    private Label signature;

    @FXML
    private Label account;

    @FXML
    private HBox searchBox;

    @FXML
    private Pane clearSearch;

    @FXML
    private TextField searchField;

    @FXML
    private SVGPath favorButtonImg;

    @FXML
    private Button favorButton;

    @FXML
    private VBox pageBox;

    @FXML
    private Button backToFavorList;

    @FXML
    private Button lastPage;

    @FXML
    private Button nextPage;

    @FXML
    private TextField jumpToPageNumber;

    @FXML
    private Button jumpToPage;

    @FXML
    private Label currentPageNumber;

    @FXML
    private Label totalPageNumber;


    @Autowired
    private WeixinSearchService searchService;

    @Autowired
    private WeixinFavorAccountsService favorAccountsService;

    /**
     * 当前显示账号
     */
    private WeixinPublicAccount currentShowAccount;

    /**
     * 搜索关键字
     */
    private String keyword;



    public enum ArticleListEvent implements Event<WeixinPublicAccount> {
        /**
         * 请求搜索某一账户
         */
        REQUIRE_SHOW_ACCOUNT,
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoggerFactory.getLogger().debug(this.getClass(),"PublicAccountController initialize");
        iniTableView();
        settingSearchBox();
        settingFavorButton();
        settingPageBox();
        initListener();
    }

    private void initListener() {

        EventDispatcher.listen(ArticleListEvent.REQUIRE_SHOW_ACCOUNT, new Listener<WeixinPublicAccount>() {

            @Override
            public void on(Event event, WeixinPublicAccount param) {

                EventDispatcher.fire(HomePageSubPanelEvent.REQUIRE_INSTALL, ArticleListPaneView.NAME);
                show(param);
            }
        });
    }

    public void show(WeixinPublicAccount weixinPublicAccount) {

        show(weixinPublicAccount,StringUtils.EMPTY);
    }

    public void show(WeixinPublicAccount weixinPublicAccount,String keyword) {
        this.keyword = keyword;
        currentShowAccount = weixinPublicAccount;
        showBriefIntroduction(weixinPublicAccount);
        stopShowLastAccountWork();
        startShowCurrentAccountWork(weixinPublicAccount,keyword);
    }

    private void showBriefIntroduction(WeixinPublicAccount weixinPublicAccount) {

        Platform.runLater(() -> {
            if (weixinPublicAccount != null) {
                headImage.setImage(new Image(weixinPublicAccount.getRoundHeadImg()));
                account.setText(weixinPublicAccount.getNickName());
                signature.setText(weixinPublicAccount.getSignature());
                tableView.setItems(FXCollections.emptyObservableList());
            }
        });

    }


    public static final String UN_FAVOR_FILL = "unFavorFill";

    public static final String FAVOR_FILL = "favorFill";

    private void customIsFavor(WeixinPublicAccount weixinPublicAccount) {

        boolean isFavor = favorAccountsService.isFavorAccount(weixinPublicAccount);
        changeFavorColor(isFavor);
    }

    private void changeFavorColor(boolean isFavor) {

        String tooltipContent = "点击收藏";
        if (isFavor) {
            favorButtonImg.getStyleClass().removeAll(UN_FAVOR_FILL);
            favorButtonImg.getStyleClass().add(FAVOR_FILL);
            tooltipContent = "取消收藏";
        } else {
            favorButtonImg.getStyleClass().removeAll(FAVOR_FILL);
            favorButtonImg.getStyleClass().add(UN_FAVOR_FILL);
        }
        Tooltip tooltip = new Tooltip();
        tooltip.setText(tooltipContent);
        favorButton.setTooltip(tooltip);
    }

    /**
     * 停止上一个公众号显示的工作
     */
    private void stopShowLastAccountWork() {

        currentPageNumber.setText("1");
        totalPageNumber.setText("1");
        jumpToPageNumber.setText("");
        pageBox.setVisible(false);
    }


    private void startShowCurrentAccountWork(WeixinPublicAccount weixinPublicAccount,String keyword) {

        WeixinArticlesInfo weixinArticlesInfo = searchService.searchPageArticles(weixinPublicAccount, keyword, 1);
        int totalCount = weixinArticlesInfo.getArticleTotalCount();
        Platform.runLater(() -> {
            customIsFavor(weixinPublicAccount);
            showTable(1,weixinArticlesInfo);
            totalNumber.setText(totalCount + "");
            showPageBox(totalCount);
        });
    }

    private void showTable(int currentPageIndex , WeixinArticlesInfo weixinArticlesInfo) {
        currentPageNumber.setText(currentPageIndex+"");
        tableView.setItems(FXCollections.observableArrayList(ListUtils.ensureNotNull(weixinArticlesInfo.getArticles())));
    }

    private void showPageBox(int totalCount) {
        int pageShowCount = searchService.getPageCount();
        if (totalCount > pageShowCount) {
            pageBox.setVisible(true);
            int p = totalCount / pageShowCount;
            totalPageNumber.setText(p + "");
        }
    }

    private void iniTableView() {

        settingTableView();
        settingTableListener();
    }

    private void settingTableView() {

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        title.prefWidthProperty().bind(tableView.widthProperty().multiply(0.7));
        updateTime.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        updateTime.setCellFactory(x -> new TimeShowTypeTableCell());
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        updateTime.setCellValueFactory(new PropertyValueFactory<>("update_time"));
    }

    private void settingTableListener() {

        tableView.setRowFactory(x -> {
            TableRow<WeixinArticle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    // 双击进行阅读书籍
                    WeixinArticle article = row.getItem();
                    readArticle(article);
                }
            });
            return row;
        });
    }

    public void readArticle(WeixinArticle article) {

        //LoggerFactory.getLogger().debug(this.getClass(),"read article {}", article);
        EventDispatcher.fire(WeixinArticleReadController.WeixinArticleReadEvent.REQUIRE_LOAD, article);
    }

    private void settingSearchBox() {

        settingSearchField();
        settingClearSearchButton();
    }

    private void settingClearSearchButton() {

        clearSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (clearSearch.isVisible()) {
                    hideClearSearch();
                    clearSearchText();
                    show(currentShowAccount);
                }
            }
        });
    }

    private void clearSearchText() {

        searchField.clear();
    }

    private void settingSearchField() {

        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.ENTER) {
                    String text = searchField.getText();
                    if (StringUtils.isNotEmpty(text)) {
                        LoggerFactory.getLogger().debug(this.getClass(),"search {}", text);
                        show(currentShowAccount,text);
                    }
                }
            }
        });
        searchField.textProperty().addListener((observable, oldText, newText) -> {
            String text = newText.trim();
            if (StringUtils.isEmpty(text)) {
                hideClearSearch();
            } else {
                showClearSearch();
            }
        });
    }

    public void showClearSearch() {

        clearSearch.setVisible(true);
    }

    public void hideClearSearch() {

        clearSearch.setVisible(false);
    }

    private void settingPageBox() {

        lastPage.setTooltip(new Tooltip("上一页"));
        lastPage.setOnMouseClicked(e -> lastPage());
        nextPage.setTooltip(new Tooltip("下一页"));
        nextPage.setOnMouseClicked(e -> nextPage());
        jumpToPage.setOnMouseClicked(e -> jumpToPage());
        backToFavorList.setTooltip(new Tooltip("返回收藏页"));
        backToFavorList.setOnMouseClicked(e -> backToFavorList());
        jumpToPageNumber.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                jumpToPage();
            }
        });
    }

    private int getCurrentPageIndex() {
        return Integer.parseInt(currentPageNumber.getText());
    }

    private int getTotalPageIndex() {

        return Integer.parseInt(totalPageNumber.getText());
    }


    public void backToFavorList() {
        EventDispatcher.asyncFire(HomePageSubPanelEvent.REQUIRE_INSTALL, FavorAccountsView.NAME);
    }

    public void lastPage() {
        int currentPageIndex = getCurrentPageIndex();
        currentPageIndex--;
        if (currentPageIndex>=1) {
            jumpToPage(currentPageIndex);
        }
    }

    public void nextPage() {
        int currentPageIndex = getCurrentPageIndex();
        int totalPageIndex = getTotalPageIndex();
        currentPageIndex++;
        if (currentPageIndex <= totalPageIndex) {
          jumpToPage(currentPageIndex);
        }
    }

    private void jumpToPage(int pageIndex) {
        WeixinArticlesInfo weixinArticlesInfo = searchService.searchPageArticles(currentShowAccount, keyword, pageIndex);
        showTable(pageIndex,weixinArticlesInfo);
    }

    public void jumpToPage() {
        int currentPageIndex = getCurrentPageIndex();
        int totalPageIndex = getTotalPageIndex();
        String j = jumpToPageNumber.getText();
        if (!NumberUtils.isDigits(j)) {
            LoggerFactory.getLogger().debug(this.getClass(),"填充的不是数字 {}",j);
        }
       // todo 需要进一步操作，先不进行
        int ji = Integer.parseInt(j);
        if (ji == currentPageIndex || ji<0 || ji >totalPageIndex) {
            return;
        }
        jumpToPage(ji);
    }

    private void settingFavorButton() {


        favorButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (currentShowAccount != null) {
                    boolean isFavor = favorAccountsService.isFavorAccount(currentShowAccount);
                    if (isFavor) {
                        favorAccountsService.removeFavor(currentShowAccount);
                        Toast.makeText("取消订阅成功", Toast.DURATION_SHORT).show(favorButton, Side.RIGHT,0,0);
                    } else {
                        favorAccountsService.addToFavor(currentShowAccount);
                        Toast.makeText("订阅成功", Toast.DURATION_SHORT).show(favorButton,Side.RIGHT,0,0);
                    }
                    changeFavorColor(!isFavor);
                }
            }
        });
    }

}
