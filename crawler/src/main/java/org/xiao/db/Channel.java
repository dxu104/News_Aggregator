package org.xiao.db;

import java.util.Date;

public class Channel {
    private String name;
    private String URL;
    private String description;
    private long createdTime;
    private long updatedTime;


    public Channel(String name, String URL, String description) {
        this.name = name;
        this.URL = URL;
        this.description = description;
        this.createdTime = new Date().getTime();;
        this.updatedTime = new Date().getTime();;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(int createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(int updatedTime) {
        this.updatedTime = updatedTime;
    }
}
