package com.example.newsaggregator.http;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.newsaggregator.models.News;
import com.example.newsaggregator.models.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * ParseTool to parse data from Http response.
 *
 * @author Jianhua Tan
 */
public class ParseTool {

    /**
     * Parse list for NewsList.
     * @param jsonArray
     * @return
     */
    public static final List<NewsItem> parseNewsList(JSONArray jsonArray) {
        List<NewsItem> newsItems = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            NewsItem newsItem = new NewsItem();

            newsItem.setID(jsonObject.getLong("ID"));
            newsItem.setChannelID(jsonObject.getLong("channelID"));
            newsItem.setCover(jsonObject.getString("cover"));
            newsItem.setCreatedTime(jsonObject.getLong("createdTime"));
            newsItem.setDescription(jsonObject.getString("description"));
            newsItem.setPostedTime(jsonObject.getLong("postedTime"));
            newsItem.setTitle(jsonObject.getString("title"));
            newsItem.setUpdatedTime(jsonObject.getLong("updatedTime"));

            newsItems.add(newsItem);
        }

        return newsItems;
    }

    /**
     * Parse jsonObject to a News Model.
     * @param jsonObject
     * @return
     */
    public static final News parseNewsDetail(JSONObject jsonObject) {
        News news = new News();

        if (jsonObject != null) {
            news.setID(jsonObject.getLong("ID"));
            news.setURL(jsonObject.getString("URL"));
            news.setAuthor(jsonObject.getString("author"));
            news.setChannelID(jsonObject.getLong("channelID"));
            news.setContent(jsonObject.getString("content"));
            news.setCover(jsonObject.getString("cover"));
            news.setCreatedTime(jsonObject.getLong("createdTime"));
            news.setDescription(jsonObject.getString("description"));
            news.setGuid(jsonObject.getString("guid"));
            news.setPostedTime(jsonObject.getLong("postedTime"));
            news.setTitle(jsonObject.getString("title"));
            news.setUpdatedTime(jsonObject.getLong("updatedTime"));
        }

        return news;
    }

}
