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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

import java.util.Random;


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

    private String[] LabelColors = {"#F9E79F", "#F5F5DC", "#FFF8DC", "#87CEFA", "#40E0D0", "#ABEBC6"};


    @FXML
    public void initialize() {
        System.out.println("initialize() NewsDetailController _________");
        Random ran = new Random();
        int corIn = ran.nextInt(6);
        label_title.setStyle("-fx-background-color: " + LabelColors[corIn % 5] + ";");
        label_info.setStyle("-fx-background-color: " + LabelColors[corIn % 5] + ";");

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
                label_title.setWrapText(true);
                label_title.setText(news.getTitle());

                Random random = new Random();
                float fla = random.nextFloat(1);
                int views = ((int) (fla * 15000));

                label_info.setText(" Posted " + ItemSmallController.formateTime(news.getPostedTime())
                        + "    |    "
                        + news.getAuthor()
                        + "    |    "
                        + views + " views");

                String content = news.getContent();
                if (content.contains("<img")) {
                    content = content.replaceAll("<img", "<img width=480px; height=270px; ");
                    webview_detail.getEngine().loadContent(content);
                } else {
                    content = "<img src=\"" + news.getCover() + "\" width=480px; height=270px; > \r" + content;
                    webview_detail.getEngine().loadContent(content);
                }


            }
        });

    }


}
