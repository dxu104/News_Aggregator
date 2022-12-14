package POJO;


/*In news class, we have 12 attributes.
1.	ID starting from 1 is primary key of news entity object
2.	channelID ranging from 1 to 5 the ID of different channel,
 such as 1 represents CNN channel, 2 represents ABC channel etc.
3.	URL represent links of news entity.
4.	Title is the news titles
5.	Author represents news authors
6.	Cover are links of picture of news entities.
7.	Content represents news content.
8.	Description is abstract of news content.
9.	PostedTime represents when news was published.
10.	createdTime represents when news was added into database.
11.	updatedTime represents when we updated our database.
12.	GUID is subscription GUID that uniquely identifies user’s
subscription to use news subscription services.
*/


import java.net.URL;

public class news {
    private Integer id;
    private Integer ChannelID;

    private Integer views;
    private String URL;
    private String title;
    private String author;
    private String cover;
    private String description;
    private String content;


    private long postedTime;
    private long createdTime;
    private long updatedTime;
    private String guid;

    public Integer getID() {
        return id;
    }

    public void setID(Integer ID) {
        this.id = ID;
    }

    public Integer getChannelID() {
        return ChannelID;
    }

    public void setChannelID(Integer channelID) {
        ChannelID = channelID;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getGuid() {
        return guid;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }


    @Override
    public String toString() {
        return "news{" +
                "id=" + id +
                ", ChannelID=" + ChannelID +
                ", views=" + views +
                ", URL='" + URL + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", postedTime=" + postedTime +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", guid='" + guid + '\'' +
                '}';
    }
}
