package com.example.newsaggregator.ui;

import com.example.newsaggregator.models.NewsItem;
import com.example.newsaggregator.widgets.BaseCell;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

import java.net.URL;

/**
 * ItemCell for ListView of Homepage and Searching response.
 *
 * @author Jianhua Tan
 */
public class ItemViewCell extends BaseCell {
    @FXML
    private ImageView imageview_cover;

    private NewsItem itemModel;



    public ItemViewCell() {
//        super(ItemViewCell.class.getResource("itemsmall-view.fxml"));
        super(null);
//        System.out.println("itemview URL = " + MainApplication.class.getClass().getResource("itemsmall-view.fxml"));
//        System.out.println("itemview URL = " + getClass().getResource("itemsmall-view.fxml"));
//        System.out.println("itemview URL = " + ItemViewCell.class.getResource("itemsmall-view.fxml"));

    }

    public ItemViewCell(URL fxmlURL) {
        super(fxmlURL);
    }

    @Override
    public void bindData(Object item) {
//        setText(((NewsItem)item).getTitle());
        if (item instanceof NewsItem) {
            NewsItem newsItem = (NewsItem) item;
            itemModel = newsItem;

            WebView webview = new WebView();
            webview.setPrefWidth(445);
            webview.setPrefHeight(130);
            webview.getEngine().loadContent(ItemSmallController.getItemHtml(newsItem));
            setGraphic(webview);

            webview.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    PopWindow.showPopWindow(ConfigController.XML_NEWS_DETAIL, newsItem.getID());
                }
            });
        }

    }

    @Override
    public Object call(Object o) {
        return null;
    }

}
