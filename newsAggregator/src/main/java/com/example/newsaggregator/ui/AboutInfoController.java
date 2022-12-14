package com.example.newsaggregator.ui;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * About Info Page of the Application.
 *
 * @author Jianhua Tan
 */
public class AboutInfoController extends BaseController {
    @FXML
    private Button button_checkupdate;


    @FXML
    public void initialize() {
        button_checkupdate.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // check updated version here when there is a api later.
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(ConfigController.APP_NAME);
                alert.setHeaderText("Check up-to-date version");
                alert.setContentText("You are now at the latest version!");
                alert.show();
            }
        });
    }

    @Override
    void start(Object... objects) {

    }

}
