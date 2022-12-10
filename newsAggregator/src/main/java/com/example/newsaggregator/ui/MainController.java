package com.example.newsaggregator.ui;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.example.newsaggregator.http.HttpRequest;
import com.example.newsaggregator.http.ParseTool;
import com.example.newsaggregator.models.NewsItem;
import com.example.newsaggregator.widgets.BaseCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.URLConverter;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.util.*;

import static javafx.scene.control.Tab.SELECTION_CHANGED_EVENT;

public class MainController {
    @FXML
    private AnchorPane anchorpane_homepage;
    @FXML
    private ImageView imageview_splash_back;
    @FXML
    private ImageView imageview_splash_icon;

    @FXML
    private Label mainTitleLabel;

    @FXML
    private TabPane tabpane_homepage;
    @FXML
    private Tab tab_seatimes;
    @FXML
    private Tab tab_foxnews;

    @FXML
    private ListView listview_seattle;

    @FXML
    private WebView webview_test;

    /**
     * listciew数据
     */
    private ObservableList<NewsItem> dataListSeattleTimes = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        listview_seattle.setItems(dataListSeattleTimes);
        testRequest();
        loadSplash();
        autoHideSplashAndShowHome();

        initListView();

        tabpane_homepage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int selected = tabpane_homepage.getSelectionModel().getSelectedIndex();
                System.out.println("你选中了 selected = " + selected);
                if (selected == 1) {
                    webview_test.getEngine().loadContent(testStr);
                }
            }
        });

        tab_seatimes.setOnSelectionChanged(new myEventHandler());
        tab_foxnews.setOnSelectionChanged(new myEventHandler());

    }


    private void loadSplash() {
        imageview_splash_back.setScaleX(1.1);
        imageview_splash_back.setScaleY(1.5);
        imageview_splash_back.setImage(new Image("splash.png"));
//        imageview_splash_back.setImage(new Image(getClass().getResourceAsStream("/image/splash.png")));
//        imageview_splash_back.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/splash.png"))));
        imageview_splash_icon.setImage(new Image("app_name.gif"));
    }

    private void autoHideSplashAndShowHome() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // update UI within UI thread
                        imageview_splash_back.setVisible(false);
                        imageview_splash_icon.setVisible(false);

                        imageview_splash_back.setImage(null);
                        imageview_splash_icon.setImage(null);
                        anchorpane_homepage.getChildren().remove(imageview_splash_back);
                        anchorpane_homepage.getChildren().remove(imageview_splash_icon);

                        autoShowHomepage();
                    }
                });
            }
        };
        new Timer().schedule(timerTask, 3000);


    }

    private void autoShowHomepage() {
        tabpane_homepage.setVisible(true);
        mainTitleLabel.setVisible(true);
    }

    private void initListView() {
//        listview_seattle.setCellFactory(new BaseCell<NewsItem>(MainApplication.class.getResource("../res/fxml/hello-view.fxml")) {

//        listview_seattle.setCellFactory(param -> new BaseCell<NewsItem>(null) {
//            @Override
//            public void bindData(NewsItem item) {
//
//            }
//        });

        listview_seattle.setCellFactory(param -> new ItemViewCell());

//        listview_seattle.setCellFactory(new Callback<ListView, ListCell>() {
//            @Override
//            public ListCell call(ListView listView) {
//                return null;
//            }
//        });


        listview_seattle.setOnScrollFinished(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.getEventType() == ScrollEvent.SCROLL_FINISHED) {
                    System.out.println("滑动完成 ————————");
                    IndexedCell indexedCell = (((VirtualFlow) listview_seattle.lookup("VirtualFlow")).getLastVisibleCell());
                    double scrollPosition = (((VirtualFlow) listview_seattle.lookup("VirtualFlow")).getPosition());
                    if (indexedCell == null) {
                        System.out.println("最后元素不可见 ———————— scrollPosition = " + scrollPosition);
                    } else {
                        System.out.println("最后元素可见 ———————— scrollPosition = " + scrollPosition);
                    }

                    // request for and load more data for next page.
                    if (scrollPosition > 0.95) {
                        System.out.println("触发添加数据 ______");
//                        addFalseDatasForSeattleTimes();
                        testRequest();
                    }
                }
            }
        });

    }

    private String testStr = "<div class=\"featured featured-video video-ct\" data-v-a7f268cc>\n" +
            " <div class=\"contain\" data-v-a7f268cc>\n" +
            "  <div class=\"control\" data-v-a7f268cc>\n" +
            "   <a href=\"#\" class=\"top\" data-v-a7f268cc></a> <a href=\"#\" class=\"close\" data-v-a7f268cc>close</a>\n" +
            "  </div>\n" +
            "  <div class=\"video-container\" data-v-a7f268cc>\n" +
            "   <div data-video-id=\"6315164598112\" data-video-domain=\"foxnews\" data-widget-type=\"embed\" class=\"m video-player\" data-v-a7f268cc>\n" +
            "    <a href=\"http://video.foxnews.com/v/6315164598112\" data-v-a7f268cc>\n" +
            "     <picture data-v-a7f268cc>\n" +
            "      <source srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/288/162/Tony-Fields.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/576/324/Tony-Fields.jpg?ve=1&amp;tl=1 2x\" media=\"(max-width: 767px)\" data-v-a7f268cc>\n" +
            "      <source srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/672/378/Tony-Fields.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1344/756/Tony-Fields.jpg?ve=1&amp;tl=1 2x\" media=\"(min-width: 768px) and (max-width: 1023px)\" data-v-a7f268cc>\n" +
            "      <source srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/676/380/Tony-Fields.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1352/760/Tony-Fields.jpg?ve=1&amp;tl=1 2x\" media=\"(min-width: 1024px) and (max-width: 1279px)\" data-v-a7f268cc>\n" +
            "      <source srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/896/500/Tony-Fields.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1792/1000/Tony-Fields.jpg?ve=1&amp;tl=1 2x\" media=\"(min-width: 1280px)\" data-v-a7f268cc>\n" +
            "      <img src=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/896/500/Tony-Fields.jpg?ve=1&amp;tl=1\" alt=\"Fox News Flash top sports headlines for December 6\" width=\"896\" height=\"500\" data-v-a7f268cc>\n" +
            "     </picture> <span class=\"overlay\" data-v-a7f268cc>Video</span></a>\n" +
            "   </div>\n" +
            "  </div>\n" +
            "  <div class=\"info\" data-v-a7f268cc>\n" +
            "   <div class=\"caption\" data-v-a7f268cc>\n" +
            "    <h4 class=\"title\" data-v-a7f268cc><a href=\"http://video.foxnews.com/v/6315164598112\" data-v-a7f268cc>Fox News Flash top sports headlines for December 6</a></h4>\n" +
            "    <p data-v-a7f268cc>Fox News Flash top sports headlines are here. Check out what's clicking on Foxnews.com.</p>\n" +
            "   </div>\n" +
            "  </div>\n" +
            " </div>\n" +
            "</div>\n" +
            "<p class=\"speakable\"><a href=\"https://www.foxnews.com/category/sports/nfl/cleveland-browns\" target=\"_blank\">Cleveland Browns</a> linebacker Tony Fields II will likely remember Sunday's game, but not the for the reason you may expect.</p>\n" +
            "<p class=\"speakable\">Fields' father, Tony Fields Sr., made arguably the play of the day when he hauled in a leaping catch after his son tossed a football into the stands following his pick-6.&nbsp;</p>\n" +
            "<p>Fields Sr. seemed to be determined to make sure the touchdown ball did not end up in the hands of a <a href=\"https://www.foxnews.com/category/sports/nfl/houston-texans\" target=\"_blank\">Houston Texans</a> fan.</p>\n" +
            "<div class=\"fox-bet-promo\" data-v-186f9ba6>\n" +
            " <a href=\"https://foxsuper6.onelink.me/y0bF?pid=Cross_sale&amp;c=FOX_News&amp;af_dp=super6%3A%2F%2F&amp;af_web_dp=https%3A%2F%2Fwww.foxsuper6.com&amp;af_ad=News-Editorial-father-browns-tony-fields-dives-touchdown-ball-stands\" target=\"_blank\" data-v-186f9ba6>\n" +
            "  <fts-responsive-image title=\"Fox Bet\" data-v-186f9ba6></fts-responsive-image>\n" +
            "  <picture data-v-186f9ba6>\n" +
            "   <source srcset=\"/_wzln/img/foxbet-native-promo.98e5400.png\" media=\"(max-width: 720px)\" data-v-186f9ba6>\n" +
            "   <source srcset=\"/_wzln/img/foxbet-native-promo@2x.d2a1d71.png\" media=\"(min-width: 719px)\" data-v-186f9ba6>\n" +
            "   <img data-v-186f9ba6>\n" +
            "  </picture></a>\n" +
            "</div>\n" +
            "<p><a href=\"https://www.foxnews.com/sports\" target=\"_blank\" rel=\"noopener noreferrer\"><strong><u>CLICK HERE FOR MORE SPORTS COVERAGE ON FOXNEWS.COM</u></strong></a></p>\n" +
            "<div baseimage=\"https://static.foxnews.com/foxnews.com/content/uploads/2022/12/Tony-Fields.jpg\" source=\"Getty Images\" class=\"image-ct inline\">\n" +
            " <div class=\"m\">\n" +
            "  <picture>\n" +
            "   <source media=\"(max-width: 767px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/343/192/Tony-Fields.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/686/384/Tony-Fields.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 767px) and (max-width: 1023px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/672/378/Tony-Fields.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1344/756/Tony-Fields.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 1024px) and (max-width: 1279px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/931/523/Tony-Fields.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1862/1046/Tony-Fields.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 1280px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/720/405/Tony-Fields.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1440/810/Tony-Fields.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <img src=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/640/320/Tony-Fields.jpg?ve=1&amp;tl=1\" alt=\"&nbsp;Tony Fields II #42 of the Cleveland Browns reacts while returning an interception for a touchdown during the fourth quarter against the Houston Texans at NRG Stadium on Dec. 4, 2022 in Houston, Texas.\">\n" +
            "  </picture>\n" +
            " </div>\n" +
            " <div class=\"caption\">\n" +
            "  <p>&nbsp;Tony Fields II #42 of the Cleveland Browns reacts while returning an interception for a touchdown during the fourth quarter against the Houston Texans at NRG Stadium on Dec. 4, 2022 in Houston, Texas. <span class=\"copyright\">(Carmen Mandato/Getty Images)</span></p>\n" +
            " </div>\n" +
            "</div>\n" +
            "<p>In a video posted on social media, Fields Sr. is seen making a falling catch. Fields had just returned an interception for a touchdown and appeared to throw the ball to his father.</p>\n" +
            "<p><a href=\"https://www.foxnews.com/sports/deshaun-watson-rusty-browns-debut-felt-every-one-those-700-days\" target=\"_blank\"><strong>DESHAUN WATSON RUSTY IN BROWNS DEBUT: 'I FELT EVERY ONE OF THOSE 700 DAYS'</strong></a></p>\n" +
            "<p>The second-year player out of <a href=\"https://www.foxnews.com/category/sports/ncaa/west-virginia-mountaineers\" target=\"_blank\">West Virginia</a> recorded three tackles, a forced fumble, a fumble recovery, and an interception in the 27-14 win over the Texans.</p>\n" +
            "<p>Fields took to Twitter to confirm that the fan who took the tumble in the stands was in fact his father.&nbsp;</p>\n" +
            "<p>\"That 'Browns fan' is my pops,\" the linebacker wrote.</p>\n" +
            "<div class=\"embed-media twitter\">\n" +
            " <div class=\"tweet-embed\"></div>\n" +
            "</div>\n" +
            "<p>Entering the game, Fields had just seven solo tackles on the season.</p>\n" +
            "<p>With just over ten minutes remaining in the third quarter and the Texans' on their own one-yard line, Fields stripped the ball from quarterback Kyle Allen. Fields teammate, Denzel Ward picked the ball up and ran four-yards into the end zone to give the Browns a 14-5 lead.</p>\n" +
            "<div baseimage=\"https://static.foxnews.com/foxnews.com/content/uploads/2022/12/Browns-Texans.jpg\" source=\"Getty Images\" class=\"image-ct inline\">\n" +
            " <div class=\"m\">\n" +
            "  <picture>\n" +
            "   <source media=\"(max-width: 767px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/343/192/Browns-Texans.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/686/384/Browns-Texans.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 767px) and (max-width: 1023px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/672/378/Browns-Texans.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1344/756/Browns-Texans.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 1024px) and (max-width: 1279px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/931/523/Browns-Texans.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1862/1046/Browns-Texans.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 1280px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/720/405/Browns-Texans.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1440/810/Browns-Texans.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <img src=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/640/320/Browns-Texans.jpg?ve=1&amp;tl=1\" alt=\"Tony Fields II #42 of the Cleveland Browns intercepts a pass, which he would return for a touchdown, during the fourth quarter against the Houston Texans at NRG Stadium on Dec. 4, 2022 in Houston, Texas.\">\n" +
            "  </picture>\n" +
            " </div>\n" +
            " <div class=\"caption\">\n" +
            "  <p>Tony Fields II #42 of the Cleveland Browns intercepts a pass, which he would return for a touchdown, during the fourth quarter against the Houston Texans at NRG Stadium on Dec. 4, 2022 in Houston, Texas. <span class=\"copyright\">(Logan Riely/Getty Images)</span></p>\n" +
            " </div>\n" +
            "</div>\n" +
            "<p>The win gave the Browns their first two game win streak this season. Cleveland improved to 5-7 on the year.</p>\n" +
            "<p>The defense and special teams made big plays on Sunday to lift the Browns to victory, highlighted by Donovan Peoples-Jones 76-yard punt return in the second quarter.</p>\n" +
            "<div baseimage=\"https://static.foxnews.com/foxnews.com/content/uploads/2022/12/Tony-Fields-Browns.jpg\" source=\"Getty Images\" class=\"image-ct inline\">\n" +
            " <div class=\"m\">\n" +
            "  <picture>\n" +
            "   <source media=\"(max-width: 767px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/343/192/Tony-Fields-Browns.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/686/384/Tony-Fields-Browns.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 767px) and (max-width: 1023px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/672/378/Tony-Fields-Browns.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1344/756/Tony-Fields-Browns.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 1024px) and (max-width: 1279px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/931/523/Tony-Fields-Browns.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1862/1046/Tony-Fields-Browns.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <source media=\"(min-width: 1280px)\" srcset=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/720/405/Tony-Fields-Browns.jpg?ve=1&amp;tl=1, https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/1440/810/Tony-Fields-Browns.jpg?ve=1&amp;tl=1 2x\">\n" +
            "   <img src=\"https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/12/640/320/Tony-Fields-Browns.jpg?ve=1&amp;tl=1\" alt=\"Tony Fields II #42 of the Cleveland Browns celebrates after returning an interception for a touchdown during the fourth quarter against the Houston Texans at NRG Stadium on Dec. 4, 2022 in Houston, Texas.\">\n" +
            "  </picture>\n" +
            " </div>\n" +
            " <div class=\"caption\">\n" +
            "  <p>Tony Fields II #42 of the Cleveland Browns celebrates after returning an interception for a touchdown during the fourth quarter against the Houston Texans at NRG Stadium on Dec. 4, 2022 in Houston, Texas. <span class=\"copyright\">(Carmen Mandato/Getty Images)</span></p>\n" +
            " </div>\n" +
            "</div>\n" +
            "<p>However, Browns <a href=\"https://www.foxnews.com/category/person/deshaun-watson\" target=\"_blank\">quarterback Deshaun Watson</a> largely struggled in his debut with the Browns against his former team. He completed 12 of his 22 pass attempts for 131 yards and threw one interception and no touchdowns.</p>\n" +
            "<p>Watson was traded from Houston to Cleveland in March as he faced more than two dozen civil lawsuits accusing him of sexual assault. Texas grand juries declined to bring charges against Watson, and he settled 23 of the lawsuits.</p>\n" +
            "<p><a href=\"https://www.foxnews.com/category/sports/nfl\" target=\"_blank\">The NFL</a> suspended Watson 11 games. He was also fined $5 million and had to undergo a mandatory treatment program after an independent arbitrator ruled that he violated the league's personal conduct policy.</p>\n" +
            "<p><a href=\"https://foxnews.onelink.me/xLDS?pid=AppArticleLink&amp;af_dp=foxnewsaf%3A%2F%2F&amp;af_web_dp=https%3A%2F%2Fwww.foxnews.com%2Fapps-products\" target=\"_blank\" rel=\"noopener noreferrer\"><strong><u>CLICK HERE TO GET THE FOX NEWS APP</u></strong></a></p>\n" +
            "<p>The Browns travel back to Ohio for a game against their division rival <a href=\"https://www.foxnews.com/category/sports/nfl/cincinnati-bengals\" target=\"_blank\">Cincinnati Bengals</a> on Dec. 11.</p> <!---->\n" +
            "<div class=\"article-meta\">\n" +
            " <div class=\"author-bio\">\n" +
            "  <p>Chantz Martin is a sports writer for Fox News Digital.</p>\n" +
            " </div>\n" +
            "</div>";

//    private static final String USER_PATH = "D:\\Test\\csglxt\\user.txt";
//    @Override
//    public List<User> querylist() {
//        List<User> userList = new ArrayList<>();
//        File file = new File(USER_PATH);
//        try (FileInputStream fis = new FileInputStream(file);
//             ObjectInputStream ois  = new ObjectInputStream(fis)){
//            userList = (List<User>) ois.readObject();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return userList;
//    }

    private class myEventHandler implements EventHandler {
        @Override
        public void handle(Event event) {
            if (event.getEventType() == SELECTION_CHANGED_EVENT) {
                int selected = tabpane_homepage.getSelectionModel().getSelectedIndex();
//                System.out.println("你选中了 selected = " + selected);
            }
        }
    }

    private void testRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resStr = HttpRequest.get("https://news.danta.fun/newsList?channelID=2&page=1&pageSize=30");

                JSONArray jsonArray = JSON.parseArray(resStr);
                List<NewsItem> newsItems = ParseTool.parseNewsList(jsonArray);
                dataListSeattleTimes.addAll(newsItems);
            }
        }).start();
    }




}
