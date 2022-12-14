package com.example.newsaggregator.ui;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


/**
 * A Controller for More Info View of the Application.
 *
 * @author Jianhua Tan
 */
public class MoreViewController extends BaseController {
    @FXML
    private ImageView imageview_avatar;

    @FXML
    private Label label_username;

    @FXML
    private Label label_savedlist;

    @FXML
    private Label label_about;


    @FXML
    public void initialize() {
        imageview_avatar.setImage(new Image("avatar.png"));
        label_username.setText("avatar");

        /**
         * Open the page of displaying articles a user saved.
         */
        label_savedlist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PopWindow.showPopWindow(ConfigController.XML_SAVED_LIST);
            }
        });

        /**
         * Open About Info Page.
         */
        label_about.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PopWindow.showPopWindow(ConfigController.XML_ABOUT_INFO);
            }
        });
    }


    @Override
    void start(Object... objects) {

    }
}
