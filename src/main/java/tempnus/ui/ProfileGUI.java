package tempnus.ui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import tempnus.logic.CommonControls;

import java.io.IOException;

public class ProfileGUI extends CommonControls {

    public void openTimetableUI(Event event) throws IOException {
        Button source = (Button) event.getSource();
        Scene main = source.getScene();
        main.setRoot(FXMLLoader.load((getClass().getResource("/layout/timetable_view.fxml"))));
    }
}
