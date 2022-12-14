package POJO;

public class userFavor {

    Integer userID;
    Integer newsID;

    public Integer getNewsID() {
        return newsID;
    }

    public void setNewsID(Integer newsID) {
        this.newsID = newsID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "userFavor{" +
                "newsID=" + newsID +
                ", userID=" + userID +
                '}';
    }
}
