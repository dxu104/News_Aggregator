package com.example.newsaggregator.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * MainApplication necessary to run the application.
 *
 * @author Jianhua Tan
 */
public class MainApplication extends Application {


    /**
     * A main method runs immediately at the beginning when Application is invoked by a user.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 800);
        stage.setTitle("News Aggregator");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Developers must define a single main method to bind it to Application. Then application would automatically call
     * this method to launch itself.
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }


}
