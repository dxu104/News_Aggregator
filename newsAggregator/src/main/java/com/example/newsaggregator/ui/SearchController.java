package com.example.newsaggregator.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


/**
 * A Controller for Search Page.
 *
 * @author Jianhua Tan
 */
public class SearchController extends BaseController{
    @FXML
    private TextField textfield_search;

    @FXML
    private Button button_search;


    @FXML
    public void initialize() {
        button_search.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                doSearch();
            }
        });
    }

    @Override
    void start(Object... objects) {
    }


    /**
     * Search news by keyword.
     */
    private void doSearch() {
        String word = textfield_search.getText();

        if (word == null || word.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please type in some words and try again.");
            alert.show();
        } else {
            PopWindow.showPopWindow(ConfigController.XML_SEARCH_LIST, word);
        }
    }

}
