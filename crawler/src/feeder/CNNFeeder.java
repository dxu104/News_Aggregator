package feeder;


import common.Utility;
import db.DataHandler;
import db.News;
import xiao.rss.Item;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.tinylog.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CNNFeeder extends BaseFeeder {

    private final Pattern postedTimePattern;

    public CNNFeeder(DataHandler dataHandler) {
        super(dataHandler);
        postedTimePattern = Pattern.compile("\\w+\\s(?<time>\\d+:\\d+)\\s(?<pm>\\w+)\\s(?<timezone>\\w+),\\s(\\w+) (?<month>\\w+)\\s(?<day>\\d+),\\s(?<year>\\d+)");
    }

    @Override
    public void initRssLink() {
        rssLink = "http://rss.cnn.com/rss/cnn_latest.rss";
    }

    @Override
    public void initNeedDoc() {
        needDoc = true;
    }

    @Override
    protected void initChannelInfo() {
        channelName = "CNN";
        channelURL = "https://www.cnn.com/";
        channelDescription = "CNN News";
    }

    @Override
    protected void initCookies() {

    }

    @Override
    protected String getAuthor(Item article, Document doc) {
        Elements authorEles = doc.select("span.byline__name");
        if (authorEles.size() > 0)
            return joinElements(authorEles);
        Element ele = doc.selectFirst("span.Authors__writer");
        if (ele != null) {
            return ele.text();
        }
        ele = doc.selectFirst("div.Article__subtitle");
        if (ele != null) {
            String eleText = ele.text();
            return eleText.split(",")[0];
        }
        Logger.error("Couldn't find a author in article : " + article.getLink().toString());
        return "";
    }

    @Override
    protected String getContent(Item article, Document doc) throws Exception {
        Element contentEle = doc.selectFirst("div.article__content");
        if (contentEle != null) {
            contentEle.filter((node, i) -> {
                if (i > 1) {
                    return NodeFilter.FilterResult.SKIP_ENTIRELY;
                }
                if (node.hasAttr("class")) {
                    String value = node.attr("class");
                    if (value.contains("related-content") || value.contains("bx")) {
                        return NodeFilter.FilterResult.REMOVE;
                    }
                }
                return NodeFilter.FilterResult.CONTINUE;

            });
            return contentEle.html();
        }
        contentEle = doc.selectFirst("div.BasicArticle__main");
        if (contentEle != null)
            return contentEle.html();
        throw new Exception("Couldn't find a content in article : " + article.getLink().toString());
    }

    @Override
    protected long getPostedTime(Item article, Document doc) {
        Element timeEle = doc.selectFirst("div.timestamp");
        if (timeEle == null) {
            Logger.error("Couldn't find a posted time in article : " + article.getLink().toString());
            return article.getDate().getTime();
//            if (article.getPubDate().isEmpty()) {
//                return 0;
//            }
//            String timeText = article.getPubDate().get();
//            String[] timeAry = timeText.split(" ");
//            int month = Utility.convertMonth2int(timeAry[2]);
//            String dateText = String.format("%s/%s/%s %s", timeAry[3], month, timeAry[1], timeAry[4]);
//            return Utility.dateText2timestamp("yy/MM/dd hh:mm:ss", dateText, timeAry[5]);
        }
        String timeText = timeEle.text();

        long ts = 0;
        Matcher m = postedTimePattern.matcher(timeText);
        if (m.find()) {
            String year = m.group("year");
            String monthText = m.group("month");
            int month = Utility.convertMonth2int(monthText);
            String day = m.group("day");
            String time = m.group("time");
            String pm = m.group("pm");
            String t = String.format("%s/%d/%s %s %s", year, month, day, time, pm);
            ts = Utility.dateText2timestamp("yy/MM/d hh:mm aa", t, m.group("timezone"));
        }
        return ts;
    }

    @Override
    protected String getDescription(Item article, Document doc) {
        return article.getDescription();
    }

    @Override
    protected String getURL(Item article, Document doc) {
        return article.getLink().toString();
    }

    @Override
    protected String getTitle(Item article, Document doc) {
        return article.getTitle();
    }

    @Override
    protected String getCover(Item article, Document doc) {
        return article.getMedia()[0].getLink().toString();
    }

    @Override
    protected boolean skip(News news) {
        return news.getURL().contains("/live-news/");
    }

}
