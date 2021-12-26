package tempnus.logic;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CommonControls {

    public void handleCloseButtonAction(ActionEvent event) {
        Button source = (Button) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public void returnHome(MouseEvent event) throws IOException{
        Label source = (Label) event.getSource();
        Scene main = source.getScene();
        main.setRoot(FXMLLoader.load((getClass().getResource("/layout/profile_view.fxml"))));

    }
}
