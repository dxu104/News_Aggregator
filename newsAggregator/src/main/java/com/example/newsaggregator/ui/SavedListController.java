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
 * A Saved List Page.
 *
 * @author Jianhua Tan
 */
public class SavedListController extends BaseController {
    @FXML
    private Label label_nosaved;

    @FXML
    private ListView listview_savedlist;

    private ObservableList<NewsItem> dataList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        label_nosaved.setText("loading...");

        label_nosaved.setVisible(true);
        listview_savedlist.setVisible(false);
        listview_savedlist.setItems(dataList);
        listview_savedlist.setCellFactory(param -> new ItemViewCell());
    }


    @Override
    void start(Object... objects) {
        requestSavedList();
    }


    /**
     * Request Saved Articles from Web Services from back end.
     */
    private void requestSavedList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resStr = HttpRequest.get("https://news.danta.fun/userFavorListServlet?userID=" + ConfigController.USER_ID);

                JSONArray jsonArray = JSON.parseArray(resStr);

                if (jsonArray != null && jsonArray.size() > 0) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // update UI within UI thread
                            label_nosaved.setVisible(false);
                            listview_savedlist.setVisible(true);

                            List<NewsItem> newsItems = ParseTool.parseNewsList(jsonArray);
                            dataList.addAll(newsItems);
                        }
                    });

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // update UI within UI thread
                            label_nosaved.setText("No result of searching");
                            label_nosaved.setVisible(true);
                            listview_savedlist.setVisible(false);
                        }
                    });
                }

            }
        }).start();
    }

}
