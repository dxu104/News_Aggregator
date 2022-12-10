package org.xiao.feeder;

import org.xiao.db.DataHandler;
import org.xiao.db.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.xiao.rss.Item;

public class SeattleTimesFeeder extends BaseFeeder {

    public SeattleTimesFeeder(DataHandler dataHandler) {
        super(dataHandler);
    }

    @Override
    public void initRssLink() {
        rssLink = "https://www.seattletimes.com/feed/";
    }

    @Override
    public void initNeedDoc() {
        needDoc = true;
    }

    @Override
    protected void initChannelInfo() {
        channelName = "Seattle Times";
        channelURL = "https://www.seattletimes.com/";
        channelDescription = "";
    }

    @Override
    protected void initCookies() {
        String s = "usprivacy=1---; _matheriSegs=MATHER_U9_INSTANTH_20200219; ajs_user_id=null; ajs_group_id=null; ajs_anonymous_id=%224b176c54-089c-4ee1-8e57-80adff65562d%22; _pnvl=false; pushly.user_puuid=uYhKV0ge6SMyi9tAOxW4ah7aB8i8dAUD; _pndnt=; _dor=www.seattletimes.com; _cb=C0AAmHBDV7veDNo-5T; __gads=ID=440f4d2e6ab3e34f:T=1668025279:S=ALNI_MYBmXV8QdG8dsW7cYnVxF9CxJsR6w; _pnlspid=4975; _gaexp=GAX1.2.1YXPlZk2TBaBl2-hxBMipg.19410.0; _gid=GA1.2.1397246706.1670199527; ln_or=d; st_utm_params=%7B%22utm_source%22%3A%22RSS%22%2C%22utm_medium%22%3A%22Referral%22%2C%22utm_campaign%22%3A%22RSS_all%22%2C%22utm_term%22%3A%22%22%2C%22utm_content%22%3A%22%22%7D; st_subscribe_page_modal_seen=true; _pnpdm=true; __gpi=UID=000009d7dff51e4c:T=1668025279:RT=1670364054:S=ALNI_MYVQ1Z82KZ5yqlTVYn3xYFBpn2p0g; g_state={\"i_p\":1670450456627,\"i_l\":2}; _matherSegments=MATHER_C1_SPORTS_20211007%2CMATHER_E1_FANATICS_20180625%2CMATHER_P3_HIGHPROPENSITYGROUPB_20190712%2CMATHER_P3_HIGHPROPENSITY_20180614%2CMATHER_U9_INSTANTH_20200219; _pnss=blocked; st-return=https%3A%2F%2Fwww.seattletimes.com%2Fnews%2Fus-court-dismisses-suit-against-saudi-prince-in-killing%2F; __insp_uid=2318028971; sessionToken=eyJlbWFpbCI6InRoYXR4aWFvY2hlbkBnbWFpbC5jb20iLCJVVUlEIjoiNjM4ZmUxNWQ3MzJhNzMuODc2MTU4OTMiLCJzZXNzaW9uSUQiOiI2MzhmZTE1ZjFiZjc0Ni42MDUxMTU5NCIsInBlcm1pc3Npb24iOiIxIiwiZXhwaXJhdGlvbiI6IjIwMjMtMDYtMDQgMTY6NDI6MDcifQ%3D%3D; _sp_uid=638fe15d732a73.87615893%2C1; st_subscriberdata=%5B%228a12952584e696d60184ea08acf56e49%22%2C1%2C0%2C1670388168%2C0%2C0%2C0%2C1%5D; _gcl_au=1.1.777825141.1670373770; _fbp=fb.1.1670373770589.1952610236; st-username=Xiao%20Chen; st_newsletter_prefs=%5B%5D; _pbjs_userid_consent_data=3524755945110770; _pubcid=d4649cbb-6494-4aa1-8ba4-043eb435ef81; panoramaId_expiry=1670979718407; _cc_id=bdcdc918578618720fc9a687da36bd3d; panoramaId=3c0ddb2218c8518db4fc66da0ae816d5393888053cea2c2568e5df10d2ba5609; _parrable_id=tpc%253A0%252CtpcUntil%253A1670461318%252CfilteredUntil%253A1670461318%252CfilterHits%253A0; cto_bidid=hi-WLV9sTmRjYmQyJTJGeFFrQiUyQmZaUU44TThoVyUyQkclMkZScHo3WmI3MnhCMUtwbnMybDJaVjlxaVdHaXRFQnNVYmtPQWpaSER6M0lqRWMlMkJVdnpFUFJQSzlSM2hsOW45ZVJnMUZBUjBkc0UxJTJGa2klMkJnSW91SzU4MFElMkYwdHRpdDIwd3o1TSUyRk9Vcg; _sp_ses.e46f=*; iss00=0; AMP_TOKEN=%24NOT_FOUND; liveramp_id_env_sampling_rate=0; _t_tests=eyJqaEJEV3BzNjBHcjNyIjp7ImNob3NlblZhcmlhbnQiOiJCIiwic3BlY2lmaWNMb2NhdGlvbiI6WyI0ZHBIaSJdfSwiU2VZSkkzMDZMWHl5SCI6eyJjaG9zZW5WYXJpYW50IjoiQSIsInNwZWNpZmljTG9jYXRpb24iOlsidmFaU2wiXX0sImtnTXluQTNDT2E1TkIiOnsiY2hvc2VuVmFyaWFudCI6IkMiLCJzcGVjaWZpY0xvY2F0aW9uIjpbIml6STlaIl19LCJpNEsxZ0pzdVNxZU5pIjp7ImNob3NlblZhcmlhbnQiOiJBIiwic3BlY2lmaWNMb2NhdGlvbiI6WyJCTGJ4dVkiXX0sImxpZnRfZXhwIjoibSJ9; _cb_svref=null; ak_bmsc=C69D6D72C884D831807367B115058090~000000000000000000000000000000~YAAQBHZiaAR+uMiEAQAALUOF6hL/7TIMqA+hqXddBQnsjVzHBSGlVuZ53A+GcWRj7gdYqlLNqg5ravaQ/d7b5Bc2oTfQ29TTK6++jFsFi815R+3Mz3vzlh+tUDndul8tBg88VVM+Py4JAIELfn+ACRa9RO59lvWeF52ncxSWYdh3MnxnQYEUU9fbz1+bJYJ0FKYIv34lj6srfZaH0aY0/vx+bF3wWN5UySIsW6eFXlbjyOBgzkc+v9C0A1QvttB77/NR+op8euq1XX5jF1Z5RDmFoUPRDaeEUJ1SsYuinOXlscceaoBsc7AgDzC/3UUPJqSxjvgJLc9XwFweAlD7xEIKCqd3/D1hmvluxf1oYisSlUURmxmPDw7cvfsQ1fSgn/smm/fWJwrxzyzHpUsDZQ==; bm_sv=4D9FACCF996847EE57C930265D6E1AB1~YAAQBHZiaBd+uMiEAQAACUWF6hJj+rIsWC+Qm89pT0FkmaMjcXvpIoEtJhcwKXkalpgViB8Lyq58DBWrcH5i24ptJZVs0sHgRzuI8LOetxleDYFo9JbhDXEIxws5tJCNHHSuyRG6bxTPwN7P4HWcKs5qGUUMaG86FK+oRP4+QOUUGO6trA2YOOOrZd6WS4PLiYb9OXNP6YobZ5JOG4oy9YXVHHsLkP06uFbYTVGlQzuGtfQDPnFGfKwlYPL6nS4pEImh3qNJ~1; __insp_wid=1902035670; __insp_slim=1670381913952; __insp_nv=false; __insp_targlpu=aHR0cHM6Ly9zZWN1cmUuc2VhdHRsZXRpbWVzLmNvbS9hY2NvdW50Y2VudGVyL21hbmFnZXN1YnNjcmlwdGlvbnM%3D; __insp_targlpt=TXkgYWNjb3VudA%3D%3D; aasd=6%7C1670381883098; OptanonAlertBoxClosed=2022-12-07T02:58:36.991Z; _ga=GA1.2.212261937.1668025273; __aaxsc=2; OptanonConsent=isIABGlobal=false&datestamp=Tue+Dec+06+2022+18%3A58%3A37+GMT-0800+(Pacific+Standard+Time)&version=6.23.0&hosts=&consentId=1e64ec4e-a4c8-42a5-b593-50d8a91987b2&interactionCount=1&landingPath=NotLandingPage&groups=SN%3A1%2CF%3A1%2CBG9%3A1%2CP%3A1%2CT%3A1%2CSM%3A1&AwaitingReconsent=false&geolocation=US%3BWA; _awl=2.1670381917.0.5-105811df3187e03eb56c32f344269cb6-6763652d75732d7765737431-0; _chartbeat2=.1668025278130.1670381919729.0100000000000011.KGw6yBD6-A1gL7weCRFi7MBKFW6r.2; _ga_3BBN1BHX44=GS1.1.1670381756.14.1.1670381919.23.0.0; cto_bundle=sB7Qo18xeUQwbkhtN2txVFdMUWhiMGphU3FYNjBTY1JyUHBHaHVoTSUyQk5iMUhkRSUyQjQyN0ZCM2VXQndqNFJVRzhuMWw4VVolMkZvR2tsMkk3NEljZjhuZzJTTENaMTlwZVdZOEI0R1FoQWtIUHBJSHlrS1NYeDUzcTllSGcwVTVOSk9DUER5M1NMM2ZXcDJ4dXZVJTJCZyUyQkVUVGFhMXZYejdaUFNxbkklMkJwU21EbXo2SmFjcDAlM0Q; _sp_id.e46f=33bbb88d03f66843.1668025274.14.1670381926.1670379174";
        for (String item : s.split(";")) {
            String[] kv = item.split("=", 2);
            cookies.put(kv[0].trim(), kv[1].trim());
        }
    }

    @Override
    protected boolean skip(News news) {
        return false;
    }

    @Override
    protected String getAuthor(Item article, Document doc) {
        return article.getAuthor();
    }

    @Override
    protected String getContent(Item article, Document doc) throws Exception {
        Element ele = doc.selectFirst("article > div > div.article-content");
        if (ele != null) {
            ele.filter((node, i) -> {
                if (i > 1) {
                    return NodeFilter.FilterResult.SKIP_ENTIRELY;
                }
                if (node.hasAttr("class")) {
                    String value = node.attr("class");
                    if (value.contains("ad-container") || value.contains("user-messaging") || value.contains("related-article")) {
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
        return article.getDate().getTime();
    }

    @Override
    protected String getDescription(Item article, Document doc) {
        Document t = Jsoup.parse(article.getDescription());
        return t.text();
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
        Document t = Jsoup.parse(article.getDescription());
        Elements c = t.getElementsByTag("img");
        if (c != null && c.size() != 0)
            return c.get(0).attr("src") ;
        else
            return "";
    }
}
