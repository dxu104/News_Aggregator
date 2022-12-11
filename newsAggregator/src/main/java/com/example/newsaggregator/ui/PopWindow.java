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

    private NewsDetailController controller;
    private static Stage primaryStage;

    /**
     * Private constructor to keep a single reference.
     */
    private PopWindow(long newsId){
        try{
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("news-detail.fxml"));
            Parent root = fxmlloader.load();
            primaryStage = new Stage();
            primaryStage.setResizable(false);      // make the size of the window unchangeable
            primaryStage.setTitle(MainController.AppName);
            primaryStage.setScene(new Scene(root,MainController.ScreenWidth, MainController.ScreenHeight));

            NewsDetailController controller = fxmlloader.getController();
            controller.start(popup, newsId);

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Show the PopWindow
     * @return
     */
    public static String showPopWindow(long newsId){
        popup = new PopWindow(newsId);

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
