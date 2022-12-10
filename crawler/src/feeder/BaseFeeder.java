package feeder;

import db.Channel;
import db.DataHandler;
import db.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.tinylog.Logger;
import xiao.rss.Feed;
import xiao.rss.Item;
import xiao.rss.Parser;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class BaseFeeder {
    protected String[] userAgents;

    protected String rssLink;

    protected boolean needDoc;

    protected int channelID;

    protected String channelName;

    protected String channelURL;

    protected String channelDescription;

    protected Map<String, String> cookies;


    public abstract void initRssLink();

    public abstract void initNeedDoc();

    protected abstract void initChannelInfo();

    protected abstract void initCookies();

    protected DataHandler dataHandler;

    public BaseFeeder(DataHandler dataHandler) {
        userAgents = new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/68.0", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:61.0) Gecko/20100101 Firefox/68.0", "Mozilla/5.0 (X11; Linux i586; rv:31.0) Gecko/20100101 Firefox/68.0"};
        initChannelInfo();
        initNeedDoc();
        initRssLink();
        cookies = new HashMap<>();
        initCookies();
        this.dataHandler = dataHandler;
        Channel channel = new Channel(channelName, channelURL, channelDescription);
        channelID = dataHandler.insertOrUpdateChannel(channel);
    }

    public int getChannelID() {
        return channelID;
    }

    public Map<String, String> getHeaders() {
        Random rd = new Random();
        String ua = userAgents[rd.nextInt(userAgents.length)];
        Map<String, String> headers = Map.of("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7", "Cache-Control", "max-age=0", "Connection", "keep-alive", "DNT", "1", "Referer", "https://www.google.com/", "Upgrade-Insecure-Requests", "1", "User-Agent", ua);

        return headers;
    }


    public int checkAndUpdate() {
        javaxt.http.Request req = new javaxt.http.Request(rssLink);
        getHeaders().forEach(req::setHeader);
//        System.out.println(req.toString());
        org.w3c.dom.Document xml = req.getResponse().getXML();
        Feed feed = new Parser(xml).getFeeds()[0]; // Usually there's only one feed or channel per document.

        Item[] articles = feed.getItems();
        return handleData(articles);
    }


    protected int handleData(Item[] articles) {
        // get all guids;
        Set<String> guids = new HashSet<>();
        for (Item article : articles) {
            guids.add(article.getGuid());
        }
        Set<String> existGuids = dataHandler.filterExistNews(guids, channelID);
        Logger.info(String.format("we got %d article, but only %d articles are new", guids.size(), guids.size() - existGuids.size()));
        List<News> newsList = new ArrayList<>();
        for (Item article : articles) {
            News news = new News();
            news.setURL(getURL(article, null));
            news.setGuid(article.getGuid());
            if (existGuids.contains(news.getGuid()) || skip(news)) continue;
            Logger.info("handling article: " + article.getTitle() + " URL: " + news.getURL());
            try {
                Document doc = null;
                long currentTime = new Date().getTime();
                if (needDoc) doc = getHTML(news.getURL());
                news.setChannelID(channelID);
                news.setTitle(getTitle(article, doc));
                news.setURL(getURL(article, doc));
                news.setCreatedTime(currentTime);
                news.setUpdatedTime(currentTime);
                news.setDescription(getDescription(article, doc));
                news.setAuthor(getAuthor(article, doc));
                news.setContent(getContent(article, doc));
                news.setPostedTime(getPostedTime(article, doc));
                news.setCover(getCover(article, doc));
                newsList.add(news);
                if (needDoc) {
                    try {
                        int seconds = 1;
                        TimeUnit.SECONDS.sleep(seconds);
                    } catch (InterruptedException e) {
                        Logger.info("Stop sleep");
                    }
                }
            } catch (Exception e) {
                Logger.error(e.getClass().getName() + ": " + e.getMessage());
            }

        }
        return dataHandler.insertOrIgnoreNews(newsList.toArray(new News[0]));
    }

    /**
     * get the html response and parse it
     * @param url
     * @return return the parsed result so we can handle it easily
     */
    protected Document getHTML(String url) {
        Document doc = null;
        try {
            if (cookies.size() > 0) {
                doc = Jsoup.connect(url).headers(getHeaders()).cookies(cookies).get();
            } else {
                doc = Jsoup.connect(url).headers(getHeaders()).get();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    // some news aren't able to collect like some live videos; so we need to skip
    protected abstract boolean skip(News news);

    protected abstract String getAuthor(Item article, Document doc);

    protected abstract String getContent(Item article, Document doc) throws Exception;

    protected abstract long getPostedTime(Item article, Document doc);

    protected abstract String getDescription(Item article, Document doc);

    protected abstract String getURL(Item article, Document doc);

    protected abstract String getTitle(Item article, Document doc);

    protected abstract String getCover(Item article, Document doc);

    protected String joinElements(Elements eles) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < eles.size(); i++) {
            sb.append(eles.get(i).text().trim());
            if (i != eles.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
