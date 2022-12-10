package com.example.newsaggregator.ui;

import com.example.newsaggregator.models.NewsItem;
import com.example.newsaggregator.widgets.BaseCell;

import java.net.URL;

public class ItemViewCell extends BaseCell {

    public ItemViewCell() {
        super(ItemViewCell.class.getResource("itemsmall-view.fxml"));
//        System.out.println("列表 itemview URL视图地址 = " + MainApplication.class.getClass().getResource("itemsmall-view.fxml"));
//        System.out.println("列表 itemview URL视图地址 = " + getClass().getResource("itemsmall-view.fxml"));
        System.out.println("列表 itemview URL视图地址 = " + ItemViewCell.class.getResource("itemsmall-view.fxml"));

    }

    public ItemViewCell(URL fxmlURL) {
        super(fxmlURL);
    }

    @Override
    public void bindData(Object item) {
//        setText(((NewsItem)item).getTitle());
        if (item instanceof ItemViewCell) {
            System.out.println("它属于 ItemViewCell _____");
        } else if (item instanceof NewsItem) {
            System.out.println("它属于 NewsItem _____");
        }
    }

    @Override
    public Object call(Object o) {
        return null;
    }

}
