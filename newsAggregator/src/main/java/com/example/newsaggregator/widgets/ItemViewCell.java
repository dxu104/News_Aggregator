package com.example.newsaggregator.widgets;

import com.example.newsaggregator.models.NewsItem;
import com.example.newsaggregator.ui.MainApplication;

import java.net.URL;

public class ItemViewCell extends BaseCell{

    public ItemViewCell() {
        super(MainApplication.class.getClass().getResource("itemview_small.fxml"));

    }

    public ItemViewCell(URL fxmlURL) {
        super(fxmlURL);
    }

    @Override
    public void bindData(Object item) {
        setText(((NewsItem)item).getTitle());
    }

    @Override
    public Object call(Object o) {
        return (NewsItem) o;
    }

}
