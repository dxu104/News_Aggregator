package org.xiao.db;

import org.jsoup.Jsoup;

import java.util.Objects;

import static org.xiao.common.Utility.escapeSql;

public class News {
    private int channelID;

    private String guid;
    private String URL;
    private String title;
    private String author;

    private String description;
    private String cover;


    private String content;
    private long postedTime;
    private long createdTime;
    private long updatedTime;


    public News() {

    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(long postedTime) {
        this.postedTime = postedTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }


    public String toString() {
        if (Objects.equals(description, "") || description.length() < 50) {
            String plaintext = Jsoup.parse(content).wholeText();
            description = plaintext.substring(0, Math.min(200,plaintext.length()));
        }
        return String.format("(%d, '%s', '%s', '%s','%s', '%s', '%s', '%s', %d, %d, %d)", channelID, escapeSql(guid), escapeSql(URL), escapeSql(title), escapeSql(cover), escapeSql(author), escapeSql(content), escapeSql(description), postedTime, createdTime, updatedTime);
    }
}
