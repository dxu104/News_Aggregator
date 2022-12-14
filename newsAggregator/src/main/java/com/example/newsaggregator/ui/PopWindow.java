package com.example.newsaggregator.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * PopWindow designed to show a pop-up window when a article should be displayed.
 *
 * @author Jianhua Tan
 */
public class PopWindow extends AnchorPane {
    private static PopWindow popup;

//    private NewsDetailController controller;
    private static Stage primaryStage;

    /**
     * Private constructor to keep a single reference.
     */
    private PopWindow(Object... objects){
        try{
            String xmlStr = (String) objects[0];
//            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("news-detail.fxml"));
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(xmlStr));
            Parent root = fxmlloader.load();
            primaryStage = new Stage();
            primaryStage.setResizable(false);      // make the size of the window unchangeable
            primaryStage.setTitle(ConfigController.APP_NAME);
            primaryStage.setScene(new Scene(root,ConfigController.ScreenWidth, ConfigController.ScreenHeight));

            if (xmlStr.equals(ConfigController.XML_NEWS_DETAIL)) {
                NewsDetailController controller = fxmlloader.getController();
                controller.start(objects[1]);

            } else if (xmlStr.equals(ConfigController.XML_SEARCH)) {
                SearchController searchController = fxmlloader.getController();
                searchController.start();

            } else if (xmlStr.equals(ConfigController.XML_SEARCH_LIST)) {
                SearchListController searchListController = fxmlloader.getController();
                searchListController.start(objects[1]);

            }


        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Show the PopWindow
     * @return
     */
    public static String showPopWindow(Object... objects){
        String xmlStr = (String)objects[0];

        if (xmlStr.equals(ConfigController.XML_NEWS_DETAIL)) {
            popup = new PopWindow(ConfigController.XML_NEWS_DETAIL, objects[1]);

        } else if (xmlStr.equals(ConfigController.XML_SEARCH)) {
            popup = new PopWindow(ConfigController.XML_SEARCH);

        } else if (xmlStr.equals(ConfigController.XML_SEARCH_LIST)) {
            popup = new PopWindow(ConfigController.XML_SEARCH_LIST, objects[1]);
        }

        if (primaryStage != null) {
            primaryStage.showAndWait();
        }

        //show the popWindow and make UI thread idle; if no need to transfer data, it can just use primaryStage.show();
        return popup.getStatus();
    }

    /**
     * Hide the PopWindow
     */
    public void hidePopWindow(){
        if (primaryStage != null) {
            primaryStage.hide();
        }
    }

    /**
     * Provide the method for other class to transfer data with it.
     * @return
     */
    public String getStatus(){
        return "200";
    }




}
