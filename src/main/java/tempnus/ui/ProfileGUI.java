package tempnus.ui;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import tempnus.logic.CommonControls;

import java.io.IOException;

public class ProfileGUI extends CommonControls {

    public void openTimetableUI(Event event) {
        SceneChanger.changeScene(event, "/layout/timetable_view.fxml");
    }

    public void openCalendarUI(Event event) throws IOException {
        Button source = (Button) event.getSource();
        Scene main = source.getScene();
        main.setRoot(FXMLLoader.load((getClass().getResource("/layout/calendar_view.fxml"))));
    }
}
