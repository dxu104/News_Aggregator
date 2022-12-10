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

public class FoxFeeder extends BaseFeeder {

    private final Pattern postedTimePattern;

    public FoxFeeder(DataHandler dataHandler) {
        super(dataHandler);
        postedTimePattern = Pattern.compile("(?<month>\\w+)\\s(?<day>\\d+),\\s*(?<year>\\d+)\\s(?<time>\\d+:\\d+)(?<pm>\\w{2})\\s(?<timezone>\\w+)");
    }

    @Override
    public void initRssLink() {
        rssLink = "https://moxie.foxnews.com/google-publisher/latest.xml";
    }

    @Override
    public void initNeedDoc() {
        needDoc = true;
    }

    @Override
    protected void initChannelInfo() {
        channelName = "Fox";
        channelURL = "https://foxnews.com/";
        channelDescription = "Fox News";
    }

    @Override
    protected void initCookies() {

    }

    @Override
    protected boolean skip(News news) {
        return false;
    }

    @Override
    protected String getAuthor(Item article, Document doc) {
        Elements elements = doc.select("#wrapper > div.page-content div.author-byline span a");
        if (elements.size() > 0) {
            String authors = joinElements(elements);
            authors = authors.replaceAll(", \\|", " \\|");
            return authors;
        }
        Logger.error("Couldn't find a author in article : " + article.getGuid());
        return "";
    }

    @Override
    protected String getContent(Item article, Document doc) throws Exception {
        Element ele = doc.selectFirst("#wrapper article > div > div.article-content > div.article-body");
        if (ele != null) {
            ele.filter((node, i) -> {
                if (i > 1) {
                    return NodeFilter.FilterResult.SKIP_ENTIRELY;
                }
                if (node.hasAttr("class")) {
                    String value = node.attr("class");
                    if (value.contains("article-footer") || value.contains("ad-container")) {
                        return NodeFilter.FilterResult.REMOVE;
                    }
                }
                return NodeFilter.FilterResult.CONTINUE;

            });
            return ele.html();
        }
        throw new Exception("Couldn't find a content in article : " + article.getGuid());
    }

    @Override
    protected long getPostedTime(Item article, Document doc) {
        Element ele = doc.selectFirst("div.article-date > time");
        if (ele != null) {
            String eleText = ele.text().trim();
            Matcher m = postedTimePattern.matcher(eleText);
            if (m.find()) {
                String year = m.group("year");
                String monthText = m.group("month");
                int month = Utility.convertMonth2int(monthText);
                String day = m.group("day");
                String time = m.group("time");
                String pm = m.group("pm");
                String t = String.format("%s/%d/%s %s %s", year, month, day, time, pm);
                return Utility.dateText2timestamp("yy/MM/d hh:mm aa", t, m.group("timezone"));
            }
        }
        Logger.error("Couldn't find a posted time in article : " + article.getGuid());
        return article.getDate().getTime();

//        if (article.getPubDate().isEmpty()) {
//            return 0;
//        }
//        String timeText = article.getPubDate().get();
//        String[] timeAry = timeText.split(" ");
//        int month = Utility.convertMonth2int(timeAry[2]);
//        String dateText = String.format("%s/%s/%s %s", timeAry[3], month, timeAry[1], timeAry[4]);
//        return Utility.dateText2timestamp("yy/MM/dd hh:mm:ss", dateText, timeAry[5]);
    }

    @Override
    protected String getDescription(Item article, Document doc) {
        return article.getDescription();
    }

    @Override
    protected String getURL(Item article, Document doc) {
        return article.getGuid();
    }

    @Override
    protected String getTitle(Item article, Document doc) {
        return article.getTitle();
    }

    @Override
    protected String getCover(Item article, Document doc) {
        return article.getMedia()[0].getLink().toString();
    }
}
