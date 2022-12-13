package com.example.newsaggregator.ui;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.example.newsaggregator.http.HttpRequest;
import com.example.newsaggregator.http.ParseTool;
import com.example.newsaggregator.models.NewsItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;


/**
 *  Search response page.
 *
 * @author Jianhua Tan
 */
public class SearchListController extends BaseController{
    @FXML
    private Label label_response;

    @FXML
    private Label label_nodata;

    @FXML
    private ListView listview_seachlist;

    private ObservableList<NewsItem> dataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        label_nodata.setText("loading...");
        listview_seachlist.setVisible(false);
        listview_seachlist.setItems(dataList);
        listview_seachlist.setCellFactory(param -> new ItemViewCell());
    }

    @Override
    void start(Object... objects) {
        String keyword = (String) objects[0];
        label_response.setText("Search \"" + keyword + "\"");

        if (keyword.contains(" ")) {
            // for Http get request, space string should be replaced by Escape character accordingly.
            keyword = keyword.replaceAll(" ", "%20");
        }
        requestSearching(keyword);
    }

    private void requestSearching(String word) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resStr = HttpRequest.get("https://news.danta.fun/newsSearch?keyWord=" + word);

                JSONArray jsonArray = JSON.parseArray(resStr);

                if (jsonArray != null && jsonArray.size() > 0) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // update UI within UI thread
                            label_nodata.setVisible(false);
                            listview_seachlist.setVisible(true);

                            List<NewsItem> newsItems = ParseTool.parseNewsList(jsonArray);
                            dataList.addAll(newsItems);
                        }
                    });

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // update UI within UI thread
                            label_nodata.setText("No result of searching");
                            label_nodata.setVisible(true);
                            listview_seachlist.setVisible(false);
                        }
                    });
                }

            }
        }).start();
    }


}
