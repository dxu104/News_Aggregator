package com.example.newsaggregator.ui;

import com.example.newsaggregator.models.NewsItem;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.Date;

/**
 * A tool to parse data to render a NewsList.
 *
 * @author Jianhua Tan
 */
public class ItemSmallController {

    /**
     * Create String data to load a webview of an ItemView.
     * @param newsItem
     * @return
     */
    public static final String getItemHtml(NewsItem newsItem) {
        if (newsItem == null) {
            return "";
        }

        String htmlStr = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>news list</title>\n" +
                "    <style type=\"text/css\">\n" +
                "        *{\n" +
                "            margin: 0;\n" +
                "            padding:0;\n" +
                "        }\n" +
                "        .wrap{\n" +
                "            width: 446px;\n" +
                "            margin: 0px auto;\n" +
                " \n" +
                "        }\n" +
                "        .menu{\n" +
                "            width: 446px;\n" +
                "            height: 30px;\n" +
                "            background: cornflowerblue;\n" +
                "            position: sticky;\n" +
                "            top:0px;\n" +
                "        }\n" +
                "        .menu ul li{\n" +
                "            float: left;\n" +
                "            list-style-type: none;\n" +
                "            padding: 0 40px;\n" +
                "        }\n" +
                "        .content ul li img:hover{\n" +
                "            transform: scale(1.2);\n" +
                "        }\n" +
                "        .content ul li{\n" +
                "            height: 110px;\n" +
                "            overflow: hidden;\n" +
                "            /* border-bottom: 1px solid lavender; */\n" +
                "            background: white;\n" +
                "            list-style-type: none;\n" +
                "            transition-duration: 0.5s;\n" +
                "            margin: 10px 10px 5px 0;\n" +
                " \n" +
                "        }\n" +
                "        .content ul li:hover{\n" +
                "            background-color: lavender;\n" +
                "            transition-duration: 0.5s;\n" +
                "        }\n" +
                "        .content .left{\n" +
                "            overflow: hidden;\n" +
                "            transition-duration: 0.5s;\n" +
                "            width: 180px;\n" +
                "            height:180px;\n" +
                "            /*background: green;*/\n" +
                "            float: left;\n" +
                "            margin-right:10px;\n" +
                "        }\n" +
                "        .content .right{\n" +
                "            width:230px ;\n" +
                "            float: left;\n" +
                "            /*background: pink;*/\n" +
                "        }\n" +
                "        .right_top{\n" +
                "            height:80px;\n" +
                "            overflow: hidden;\n" +
                "            text-overflow: ellipsis;\n" +
                "            display: -webkit-box;\n" +
                "            -webkit-line-clamp: 3;\n" +
                "            -webkit-box-orient: vertical;\n" +
                "        }\n" +
                "        .right_bottom{\n" +
                "            margin_top:50px;\n" +
                "        }\n" +
                "        .right_bottom_left span{\n" +
                "            color: darkgray;\n" +
                "            font-size: 12px;\n" +
                "        }\n" +
                "        img {  \n" +
                "    \t\twidth: auto;  \n" +
                "    \t\theight: auto;  \n" +
                "    \t\tmax-width: 100%;  \n" +
                "    \t\tmax-height: 100%;     \n" +
                "\t\t} \n" +
                "\t\tbody {\n" +
                "    \t\toverflow-x: hidden;\n" +
                "   \t\t\toverflow-y: hidden;\n" +
                "\t\t}\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body return false;\" onselectstart=\"return false\">\n" +
                "<div class=\"wrap\">\n" +
                "    \n" +
                "    <div class=\"content\">\n" +
                "        <ul>\n" +
                "            <li>\n" +
                "                <div class=\"left\"><img src=\""
                + newsItem.getCover()
                + "\" alt=\"\"></div>\n" +
                "                <div class=\"right\">\n" +
                "                    <div class=\"right_top\">\n" +
                "                        <h3>"
                + newsItem.getTitle()
                + "</h3>\n" +
                "                    </div>\n" +
                "                    <div class=\"right_bottom\">\n" +
                "                        <div class=\"right_bottom_left\">\n" +
                "                            <span> </span>  <span> </span>  <span> </span> <span> </span> <span>"
                + formateTime(newsItem.getPostedTime())
                + "</span>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </li>\n" +
                "        </ul>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        return htmlStr;
    }

    private static final String formateTime(Long time) {
        Date dateparam = new Date(time);

        Date currentTime = new Date(System.currentTimeMillis());

        switch (currentTime.getDate() - dateparam.getDate()) {
            case 0:
                int i = currentTime.getHours() - dateparam.getHours();
                if (i > 0) {
                    int i2 = currentTime.getMinutes() - dateparam.getMinutes();
                    if (i2 > 0)
                        return i + " hours ago";
                    else if (i2 > -60)
                        return 60 + i2 + " minutes ago";
                    else return "just now";
                } else {
                    int i1 = currentTime.getMinutes() - dateparam.getMinutes();
                    if (i1 > 0) {
                        return i1 + "minutes ago";
                    } else
                        return "just now";
                }
            case 1:
                return "Yesterday" + formateStr(dateparam.getHours() + "") + ":" + formateStr(dateparam.getMinutes() + "") + ":" + formateStr("" + dateparam.getSeconds());
            case 2:
                return "2 days ago" + formateStr(dateparam.getHours() + "") + ":" + formateStr(dateparam.getMinutes() + "") + ":" + formateStr("" + dateparam.getSeconds());
            default:
                return currentTime.getDate() - dateparam.getDate() + " days ago";
        }
    }

    private static final String formateStr(String str) {
        return new String().format("%02d", str);
    }

}
