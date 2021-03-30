package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.log.LoggerFactory;
import com.asa.third.org.apache.commons.lang3.math.NumberUtils;
import com.asa.utils.ListUtils;
import com.asa.utils.StringUtils;
import com.asa.weixin.spider.model.WeixinArticle;
import com.asa.weixin.spider.model.WeixinArticlesInfo;
import com.asa.weixin.spider.model.WeixinPublicAccount;
import com.asa.weixin.spider.service.WeixinFavorAccountsService;
import com.asa.weixin.spider.service.WeixinSearchService;
import com.asa.weixin.spider.ui.component.Toast;
import com.asa.weixin.spider.view.ArticleListPaneView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollToEvent;
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
import java.util.ArrayList;
import java.util.List;
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

    private WeixinPublicAccount currentShowAccount;

    /**
     * 每页显示多少条数据
     */
    public int pageShowCount = 10;

    public enum ArticleListEvent implements Event<WeixinPublicAccount> {
        /**
         * 请求搜索某一账户
         */
        REQUIRE_SHOW_ACCOUNT,
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoggerFactory.getLogger().debug("PublicAccountController initialize");
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

        currentShowAccount = weixinPublicAccount;
        showBriefIntroduction(weixinPublicAccount);
        stopShowLastAccountWork();
        startShowCurrentAccountWork(weixinPublicAccount);
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


    private void startShowCurrentAccountWork(WeixinPublicAccount weixinPublicAccount) {

        WeixinArticlesInfo weixinArticlesInfo = searchArticles(weixinPublicAccount, 1);
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

        if (totalCount > pageShowCount) {
            pageBox.setVisible(true);
            int p = totalCount / pageShowCount;
            totalPageNumber.setText(p + "");
        }
    }

    private WeixinArticlesInfo searchArticles(WeixinPublicAccount weixinPublicAccount, int page) {

        int startIndex = (page - 1) * pageShowCount;
        int lastIndex = startIndex + pageShowCount - 1;
        WeixinArticlesInfo weixinArticlesInfo = searchService.searchArticle(weixinPublicAccount, startIndex, pageShowCount);
        int totalCount = weixinArticlesInfo.getArticleTotalCount();
        int resultCount = ListUtils.length(weixinArticlesInfo.getArticles());
        int resultIndex = startIndex + resultCount - 1;
        // 一次中
        if (lastIndex == resultIndex) {
            return weixinArticlesInfo;
        }
        // 直接去最小，确保不越界
        lastIndex = Math.min(lastIndex, totalCount - 1);
        // 多了
        if (lastIndex < resultIndex) {
            // 截取一页直接返回
            weixinArticlesInfo.setArticles(weixinArticlesInfo.getArticles().subList(0, pageShowCount));
        }
        // 少了,只再试一次，其它不管
        if (lastIndex > resultIndex) {
            // 坑爹的腾讯能确保有5条记录，但是无法确保每次一样 ++++
            if (resultCount >= 5) {
                resultIndex = resultIndex -(resultCount-5);
                weixinArticlesInfo.setArticles(weixinArticlesInfo.getArticles().subList(0, 5));
            }
            int les = lastIndex - resultIndex;
            startIndex = startIndex + 5;
            WeixinArticlesInfo ex = searchService.searchArticle(weixinPublicAccount, startIndex, pageShowCount);
            List<WeixinArticle> ar =  ex.getArticles();
            int exLent = ListUtils.length(ar);
            if (exLent >0) {
                int ti = Math.min(les, exLent);
                weixinArticlesInfo.addArticles(ar.subList(0,ti));
            }
        }
        return weixinArticlesInfo;
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

        LoggerFactory.getLogger().debug("read article {}", article);
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
                        LoggerFactory.getLogger().debug("search {}", text);
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

        lastPage.setOnMouseClicked(e -> lastPage());
        nextPage.setOnMouseClicked(e -> nextPage());
        jumpToPage.setOnMouseClicked(e -> jumpToPage());
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
        WeixinArticlesInfo weixinArticlesInfo = searchArticles(currentShowAccount, pageIndex);
        showTable(pageIndex,weixinArticlesInfo);
    }

    public void jumpToPage() {
        int currentPageIndex = getCurrentPageIndex();
        int totalPageIndex = getTotalPageIndex();
        String j = jumpToPageNumber.getText();
        if (!NumberUtils.isDigits(j)) {
            LoggerFactory.getLogger().debug("填充的不是数字 {}",j);
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
