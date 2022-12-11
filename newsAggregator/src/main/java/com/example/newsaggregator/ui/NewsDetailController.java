package com.example.newsaggregator.ui;


import com.alibaba.fastjson2.JSONObject;
import com.example.newsaggregator.http.HttpRequest;
import com.example.newsaggregator.http.ParseTool;
import com.example.newsaggregator.models.News;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;



/**
 * A controller for news-detail.
 *
 * @author Jianhua Tan
 */
public class NewsDetailController {
    @FXML
    private Label label_title;
    @FXML
    private Label label_info;
    @FXML
    private WebView webview_detail;
    @FXML
    private Button button_save;
    @FXML
    private Button button_share;
    @FXML
    private Button button_more;

    private long newsId;


    @FXML
    public void initialize() {
        System.out.println("initialize() NewsDetailController _________");


    }

    /**
     * The start method is used to communicate with PopWindow.
     *
     * @param popup
     */
    public void start(PopWindow popup, long nId){
        // get data and process, then hide the popwindow
        // popup.hide();
        newsId = nId;
        requestNewsDetail(newsId);
    }

    private void requestNewsDetail(long newsId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resStr = HttpRequest.get("https://news.danta.fun/newsEntity?ID=" + newsId);
                JSONObject jsonObject = JSONObject.parseObject(resStr);
                News news = ParseTool.parseNewsDetail(jsonObject);
                updateViews(news);
            }
        }).start();
    }


    /**
     * Update Views with data from Http response.
     * @param news
     */
    private void updateViews(News news) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label_title.setText(news.getTitle());
                webview_detail.getEngine().loadContent(news.getContent());
            }
        });

    }


}
