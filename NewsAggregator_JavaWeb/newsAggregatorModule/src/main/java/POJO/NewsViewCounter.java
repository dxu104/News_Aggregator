package POJO;

import java.sql.Timestamp;

public class NewsViewCounter {
    private Integer newsID;
    private Integer count=0;
    private Timestamp updatedTime;

    public Integer getNewsID() {
        return newsID;
    }

    public void setNewsID(Integer newsID) {
        this.newsID = newsID;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "NewsViewCounter{" +
                "newsID=" + newsID +
                ", count=" + count +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
