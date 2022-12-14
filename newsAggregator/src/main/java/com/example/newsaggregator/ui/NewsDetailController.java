package com.example.newsaggregator.ui;


import com.alibaba.fastjson2.JSONObject;
import com.example.newsaggregator.http.HttpRequest;
import com.example.newsaggregator.http.ParseTool;
import com.example.newsaggregator.models.News;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Random;


/**
 * A controller for news-detail.
 *
 * @author Jianhua Tan
 */
public class NewsDetailController extends BaseController{
    @FXML
    private Label label_title;
    @FXML
    private Label label_info;
    @FXML
    private WebView webview_detail;
    @FXML
    private Button button_save;
    @FXML
    private Button button_share;
    @FXML
    private Button button_more;

    private long newsId;
    private News news;      // News model loaded in the article
    private boolean isSave;

    /**
     * List of Colors to render randomly the background of title.
     */
    private final String[] LabelColors = {"#F9E79F", "#F5F5DC", "#FFF8DC", "#87CEFA", "#40E0D0",
            "#ABEBC6", "#FFE4E1", "#FFFACD", "#E6E6FA", "#FFDAB9"};


    @FXML
    public void initialize() {
        Random ran = new Random();
        int corIn = ran.nextInt(10);
        label_title.setStyle("-fx-background-color: " + LabelColors[corIn % 10] + ";");
        label_info.setStyle("-fx-background-color: " + LabelColors[corIn % 10] + ";");


        /**
         * Click the button to share the link of the News.
         */
        button_share.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (news != null) {
                    if (news.getURL() != null && news.getURL().length() > 0) {
                        copyString2Clipboard(news.getURL());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(ConfigController.APP_NAME);
                        alert.setHeaderText("The link of the article is successfully copied!");
                        alert.setContentText("Please share the link with your friends by pasting it anywhere.");
                        alert.show();
                    }
                }
            }
        });

        /**
         * Open URL on the system browser.
         */
        button_more.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (news != null) {
                    if (news.getURL() != null && news.getURL().length() > 0) {
                        try {
                            java.net.URI uri = java.net.URI.create(news.getURL());
                            java.awt.Desktop dp = java.awt.Desktop.getDesktop();    // acquire the desktop browser
                            // check if there is a browser which could be used to open URL
                            if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                                dp.browse(uri);
                            }
                        } catch (java.lang.NullPointerException e1) {
                            e1.printStackTrace();
                        } catch (java.io.IOException e1) {
                            // when it can not find a default browser from the system
                            e1.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(ConfigController.APP_NAME);
                        alert.setHeaderText("No link exist for the article!");
                        alert.setContentText("Sorry, it seems that there is not a link with the article.");
                        alert.show();
                    }
                }
            }
        });


    }

    /**
     * The start method is used to communicate with PopWindow.
     *
     * @param objects
     */
    public void start(Object... objects){
        // get data and process, then hide the popwindow
        // popup.hide();

        newsId = (long) objects[0];
        requestNewsDetail(newsId);

        requestCheckSaved(newsId);

    }

    private void requestNewsDetail(long newsId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resStr = HttpRequest.get("https://news.danta.fun/newsEntity?ID=" + newsId);
                JSONObject jsonObject = JSONObject.parseObject(resStr);
                news = ParseTool.parseNewsDetail(jsonObject);
                updateViews(news);
            }
        }).start();
    }


    /**
     * Update Views with data from Http response.
     * @param news
     */
    private void updateViews(News news) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label_title.setWrapText(true);
                label_title.setText(news.getTitle());

                Random random = new Random();
                float fla = random.nextFloat(1);
                int views = ((int) (fla * 15000));

                label_info.setText(" Posted " + ItemSmallController.formateTime(news.getPostedTime())
                        + "    |    "
                        + news.getAuthor()
                        + "    |    "
                        + views + " views");

                String content = news.getContent();
                if (content.contains("<img")) {
                    content = content.replaceAll("<img", "<img width=480px; height=270px; ");
                    webview_detail.getEngine().loadContent(content);
                } else {
                    content = "\r<img src=\"" + news.getCover() + "\" width=480px; height=270px; > \r" + content;
                    webview_detail.getEngine().loadContent(content);
                }


            }
        });

    }

    /**
     * Copy data to clipboard the system.
     * @param text
     */
    public static void copyString2Clipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // capsule text to model
        Transferable trans = new StringSelection(text);
        // copy data to clipboard
        clipboard.setContents(trans, null);
    }

    /**
     * Request response to check if it is saved or not.
     * @param newsId
     */
    private void requestCheckSaved(long newsId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resStr = HttpRequest.get("https://news.danta.fun/isSaved?userID=" + ConfigController.USER_ID
                        + "&newsID=" + newsId);

                JSONObject jsonObject = JSONObject.parseObject(resStr);
                String code = jsonObject.getString("code");

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (code.equals("1")) {     // alredy saved
                            button_save.setText("Saved");
                            isSave = true;
                        } else {        // not saved
                            button_save.setText("Save");
                            isSave = false;
                        }

                        /**
                         * Click to add a News to saved list with service of back end.
                         */
                        button_save.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if (isSave) {
                                    requestToSave(newsId, false);
                                } else {
                                    requestToSave(newsId, true);
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }


    /**
     * Add the article to be saved list of service in back end.
     * @param newsId
     */
    private void requestToSave(long newsId, boolean save) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String saveSTr = "1";       // 1 - save; 2 - unsave;
                if (!save) {
                    saveSTr = "2";
                }
                String resStr = HttpRequest.get("https://news.danta.fun/userFavor?userID=" + ConfigController.USER_ID
                        + "&newsID=" + newsId + "&collect=" + saveSTr);
                JSONObject jsonObject = JSONObject.parseObject(resStr);

                if (jsonObject != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (save) {     // alredy saved
                                button_save.setText("Saved");
                                isSave = true;

                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle(ConfigController.APP_NAME);
                                alert.setHeaderText("Message");
                                alert.setContentText("Save the article successfully!");
                                alert.show();

                            } else {        // not saved
                                button_save.setText("Save");
                                isSave = false;

                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle(ConfigController.APP_NAME);
                                alert.setHeaderText("Message");
                                alert.setContentText("Un-save the article successfully!");
                                alert.show();
                            }
                        }
                    });
                }

            }
        }).start();
    }



}
