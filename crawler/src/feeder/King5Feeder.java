package feeder;

import common.Utility;
import db.DataHandler;
import db.News;
import xiao.rss.Item;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeFilter;
import org.tinylog.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class King5Feeder extends BaseFeeder {

    private final Pattern postedTimePattern;

    public King5Feeder(DataHandler dataHandler) {
        super(dataHandler);
        postedTimePattern = Pattern.compile("(?<time>\\d+:\\d+)\\s(?<pm>\\w{2})\\s(?<timezone>\\w+)\\s(?<month>\\w+)\\s(?<day>\\d+),\\s*(?<year>\\d+)");
    }

    @Override
    public void initRssLink() {
        rssLink = "https://www.king5.com/feeds/syndication/rss/news/local";
    }

    @Override
    public void initNeedDoc() {
        needDoc = true;
    }

    @Override
    protected void initChannelInfo() {
        channelName = "King 5";
        channelURL = "https://www.king5.com/";
        channelDescription = "King 5 News";
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
        Element ele = doc.selectFirst("article > aside > div.article__author");
        if (ele != null)
            return ele.ownText();
        Logger.error("Couldn't find a author in article : " + article.getLink().toString());
        return "";
    }

    @Override
    protected String getContent(Item article, Document doc) throws Exception {
        Element ele = doc.selectFirst("#main article > div.article__body");
        if (ele != null) {
            ele.filter((node, i) -> {
                if (i > 1) {
                    return NodeFilter.FilterResult.SKIP_ENTIRELY;
                }
                if (node.hasAttr("class")) {
                    String value = node.attr("class");
                    if (value.contains("article__section_type_embed") || value.contains("related-stories") || value.contains("article__section_type_ad")) {
                        return NodeFilter.FilterResult.REMOVE;
                    }
                }
                return NodeFilter.FilterResult.CONTINUE;

            });
            return ele.html();
        }
        throw new Exception("Couldn't find a content in article : " + article.getLink().toString());
    }

    @Override
    protected long getPostedTime(Item article, Document doc) {
        Element ele = doc.selectFirst("article > aside > div.article__published");
        if (ele != null) {
            String eleText = ele.ownText();
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
        Logger.error("Couldn't find a author in article : " + article.getLink().toString());
        return 0;
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
}
