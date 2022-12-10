package org.xiao.feeder;

import org.xiao.common.Utility;
import org.xiao.db.DataHandler;
import org.xiao.db.News;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.tinylog.Logger;
import org.xiao.rss.Item;

import java.util.Date;

public class ABCFeeder extends BaseFeeder {
    public ABCFeeder(DataHandler dataHandler) {
        super(dataHandler);
    }

    @Override
    public void initRssLink() {
        rssLink = "https://abcnews.go.com/abcnews/topstories";
    }

    @Override
    public void initNeedDoc() {
        needDoc = true;
    }

    @Override
    protected void initChannelInfo() {
        channelName = "ABC";
        channelURL = "https://abcnews.go.com/";
        channelDescription = "ABC News";
    }

    @Override
    protected void initCookies() {

    }

    @Override
    protected boolean skip(News news) {

        return news.getURL().contains("/Live/") || news.getURL().contains("video") || news.getURL().contains("/live-updates/");
    }

    @Override
    protected String getAuthor(Item article, Document doc) {
        Elements eles = doc.select("#themeProvider div.rQqmM.HZPhA span > a.uOtk.efBMc.LIsPj.wIScF");
        if (eles.size() != 0) {
            return joinElements(eles);
        } else {
            Element ele = doc.selectFirst("div.rQqmM.HZPhA > div > div.iDmZj.bGbFC.xjUv.QZvgL.zZhuN.VfZP.pqDnw.SyiTy > span:nth-child(2)");
            if (ele != null) {
                return ele.text();
            }
        }
        Logger.error("Couldn't find author name for article: ", article.getLink().toString());
        return "";
    }

    @Override
    protected String getContent(Item article, Document doc) throws Exception {
        Element ele = doc.selectFirst("article.Dyur.WVZpm.eWSik.DmCrL.WdpDx");
        if (ele != null) {
            ele.filter((node, i) -> {
                if (i > 1) {
                    return NodeFilter.FilterResult.SKIP_ENTIRELY;
                }
                if (node.hasAttr("data-testid") && node.attr("data-testid").contains("prism-grid-column")) {
                    for (Node child : node.childNodes()) {
                        if (child.hasAttr("data-testid") && child.attr("data-testid").contains("prism-ad-wrapper")) {
                            return NodeFilter.FilterResult.REMOVE;
                        }
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
        Element timeEle = doc.selectFirst("#themeProvider div.xTlfF");
        if (timeEle == null) {
            Logger.error("Couldn't find a posted time in article : " + article.getLink().toString());
            return new Date().getTime();
        }
        String timeText = timeEle.text().trim();
        String[] timeAry = timeText.split(" ", 2);
        int month = Utility.convertMonth2int(timeAry[0]);
        String dateText = month + " " + timeAry[1];
        return Utility.dateText2timestamp("M d, yy, hh:mm aa", dateText);
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
