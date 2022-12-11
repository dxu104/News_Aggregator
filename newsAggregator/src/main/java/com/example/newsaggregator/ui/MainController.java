package com.example.newsaggregator.ui;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.example.newsaggregator.http.HttpRequest;
import com.example.newsaggregator.http.ParseTool;
import com.example.newsaggregator.models.NewsItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.util.*;

import static javafx.scene.control.Tab.SELECTION_CHANGED_EVENT;

/**
 * MainController for Main Application. It is necessary to run the application.
 *
 * @author alex
 */
public class MainController {
    @FXML
    private AnchorPane anchorpane_homepage;
    @FXML
    private ImageView imageview_splash_back;
    @FXML
    private ImageView imageview_splash_icon;

    @FXML
    private Label mainTitleLabel;

    @FXML
    private TabPane tabpane_homepage;
    @FXML
    private Tab tab_seatimes;
    @FXML
    private Tab tab_foxnews;
    @FXML
    private Tab tab_cnn;
    @FXML
    private Tab tab_abc;
    @FXML
    private Tab tab_king5;

    @FXML
    private ListView listview_seattle;
    @FXML
    private ListView listview_cnn;
    @FXML
    private ListView listview_fox;
    @FXML
    private ListView listview_abc;
    @FXML
    private ListView listview_king5;

    private final int ChannelCNN = 1;
    private final int ChannelABC = 2;
    private final int ChannelFox = 3;
    private final int ChannelKing5 = 4;
    private final int ChannelSeattleTimes = 5;

    private int currentChannel = ChannelSeattleTimes;
    private int currentTabIndex = 0;

    private int pageSeattle = 0;
    private int pageCNN = 0;
    private int pageFox = 0;
    private int pageKing5 = 0;
    private int pageABC = 0;
    private final int pageSize = 10;

    /**
     * Data for each listciew.
     */
    private ObservableList<NewsItem> dataListSeattleTimes = FXCollections.observableArrayList();
    private ObservableList<NewsItem> dataListCNN = FXCollections.observableArrayList();
    private ObservableList<NewsItem> dataListFox = FXCollections.observableArrayList();
    private ObservableList<NewsItem> dataListABC = FXCollections.observableArrayList();
    private ObservableList<NewsItem> dataListKing5 = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        currentChannel = ChannelSeattleTimes;

        initListView();

        requestNewsList();
        loadSplash();
        autoHideSplashAndShowHome();

        tabpane_homepage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int selected = tabpane_homepage.getSelectionModel().getSelectedIndex();
                System.out.println("selected = " + selected);
            }
        });

        tab_seatimes.setOnSelectionChanged(new myEventHandler());
        tab_cnn.setOnSelectionChanged(new myEventHandler());
        tab_foxnews.setOnSelectionChanged(new myEventHandler());
        tab_abc.setOnSelectionChanged(new myEventHandler());
        tab_king5.setOnSelectionChanged(new myEventHandler());

    }


    private void loadSplash() {
        imageview_splash_back.setScaleX(1.1);
        imageview_splash_back.setScaleY(1.5);
        imageview_splash_back.setImage(new Image("splash.png"));
//        imageview_splash_back.setImage(new Image(getClass().getResourceAsStream("/image/splash.png")));
//        imageview_splash_back.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/splash.png"))));
        imageview_splash_icon.setImage(new Image("app_name.gif"));
    }

    private void autoHideSplashAndShowHome() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // update UI within UI thread
                        imageview_splash_back.setVisible(false);
                        imageview_splash_icon.setVisible(false);

                        imageview_splash_back.setImage(null);
                        imageview_splash_icon.setImage(null);
                        anchorpane_homepage.getChildren().remove(imageview_splash_back);
                        anchorpane_homepage.getChildren().remove(imageview_splash_icon);

                        autoShowHomepage();
                    }
                });
            }
        };
        new Timer().schedule(timerTask, 3500);


    }

    private void autoShowHomepage() {
        tabpane_homepage.setVisible(true);
        mainTitleLabel.setVisible(true);
    }

    private void initListView() {
        listview_seattle.setItems(dataListSeattleTimes);
        listview_cnn.setItems(dataListCNN);
        listview_fox.setItems(dataListFox);
        listview_abc.setItems(dataListABC);
        listview_king5.setItems(dataListKing5);

        listview_seattle.setCellFactory(param -> new ItemViewCell());
        listview_cnn.setCellFactory(param -> new ItemViewCell());
        listview_fox.setCellFactory(param -> new ItemViewCell());
        listview_abc.setCellFactory(param -> new ItemViewCell());
        listview_king5.setCellFactory(param -> new ItemViewCell());

        listview_seattle.setOnScrollFinished(new loadMoreListener());
        listview_cnn.setOnScrollFinished(new loadMoreListener());
        listview_fox.setOnScrollFinished(new loadMoreListener());
        listview_abc.setOnScrollFinished(new loadMoreListener());
        listview_king5.setOnScrollFinished(new loadMoreListener());

//        listview_seattle.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observableValue, Object o, Object t1) {
//                if (observableValue.getValue() instanceof NewsItem) {
//                    System.out.println("click  _____");
//                }
//            }
//        });

    }

    private class loadMoreListener implements EventHandler<ScrollEvent> {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.getEventType() == ScrollEvent.SCROLL_FINISHED) {
//                    IndexedCell indexedCell = (((VirtualFlow) listview_seattle.lookup("VirtualFlow")).getLastVisibleCell());
                double scrollPosition;
                if (currentChannel == ChannelSeattleTimes) {
                    scrollPosition = (((VirtualFlow) listview_seattle.lookup("VirtualFlow")).getPosition());
                } else if (currentChannel == ChannelCNN) {
                    scrollPosition = (((VirtualFlow) listview_cnn.lookup("VirtualFlow")).getPosition());
                } else if (currentChannel == ChannelFox) {
                    scrollPosition = (((VirtualFlow) listview_fox.lookup("VirtualFlow")).getPosition());
                } else if (currentChannel == ChannelABC) {
                    scrollPosition = (((VirtualFlow) listview_abc.lookup("VirtualFlow")).getPosition());
                } else {
                    scrollPosition = (((VirtualFlow) listview_king5.lookup("VirtualFlow")).getPosition());
                }

                // request for and load more data for next page.
                if (scrollPosition > 0.95) {    // load more data for next page
                    requestNewsList();
                }
            }
        }
    }



    private class myEventHandler implements EventHandler {
        @Override
        public void handle(Event event) {
            if (event.getEventType() == SELECTION_CHANGED_EVENT) {
                int selected = tabpane_homepage.getSelectionModel().getSelectedIndex();

                if (currentTabIndex == selected) {
                    return;
                }
                currentTabIndex = selected;

                // successfully select currtent tab pane.
                if (selected == 0) {
                    currentChannel = ChannelSeattleTimes;
                    if (dataListSeattleTimes.size() == 0) {
                        requestNewsList();
                    }

                } else if (selected == 1) {
                    currentChannel = ChannelFox;
                    if (dataListFox.size() == 0) {
                        requestNewsList();
                    }

                } else if (selected == 2) {
                    currentChannel = ChannelCNN;
                    if (dataListCNN.size() == 0) {
                        requestNewsList();
                    }

                } else if (selected == 3) {
                    currentChannel = ChannelABC;
                    if (dataListABC.size() == 0) {
                        requestNewsList();
                    }

                } else if (selected == 4) {
                    currentChannel = ChannelKing5;
                    if (dataListKing5.size() == 0) {
                        requestNewsList();
                    }
                }
            }
        }
    }

    private void requestNewsList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resStr = HttpRequest.get(getNewsListUrl(currentChannel));

                JSONArray jsonArray = JSON.parseArray(resStr);
                List<NewsItem> newsItems = ParseTool.parseNewsList(jsonArray);
                updateDataList(currentChannel, newsItems);
            }
        }).start();
    }

    private void updateDataList(int channelId, List<NewsItem> newsItems) {
        if (channelId == 1) {       // CNN
            dataListCNN.addAll(newsItems);

        } else if (channelId == 2) {        // ABC News
            dataListABC.addAll(newsItems);

        } else if (channelId == 3) {        // Fox News
            dataListFox.addAll(newsItems);

        } else if (channelId == 4) {        // King5 News
            dataListKing5.addAll(newsItems);

        } else if (channelId == 5) {        // Seattle News
            dataListSeattleTimes.addAll(newsItems);

        }
    }

    /**
     * Get real-time url to send a Http request.
     * @param channelId
     * @return
     */
    private String getNewsListUrl(int channelId) {
        String str = "https://news.danta.fun/newsList?channelID=";
        if (channelId == 1) {       // CNN
            pageCNN++;
            str = str + ChannelCNN + "&pageSize=" + pageSize + "&page=" + pageCNN;

        } else if (channelId == 2) {        // ABC News
            pageABC++;
            str = str + ChannelABC + "&pageSize=" + pageSize + "&page=" + pageABC;

        } else if (channelId == 3) {        // Fox News
            pageFox++;
            str = str + ChannelFox + "&pageSize=" + pageSize + "&page=" + pageFox;

        } else if (channelId == 4) {        // King5 News
            pageKing5++;
            str = str + ChannelKing5 + "&pageSize=" + pageSize + "&page=" + pageKing5;

        } else if (channelId == 5) {        // Seattle News
            pageSeattle++;
            str = str + ChannelSeattleTimes + "&pageSize=" + pageSize + "&page=" + pageSeattle;

        }

        return str;
    }



}
