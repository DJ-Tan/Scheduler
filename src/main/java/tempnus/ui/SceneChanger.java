package tempnus.ui;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import tempnus.logger.AppLogger;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

public class SceneChanger {

    public static void changeScene(Event event, String fxmlFilePath) {
        Button source = (Button) event.getSource();
        Scene main = source.getScene();
        URL url = SceneChanger.class.getResource(fxmlFilePath);
        assert url != null;
        try {
            main.setRoot(FXMLLoader.load(url));
        } catch (IOException e) {
            AppLogger.log(Level.WARNING, e.getMessage());
        }

    }

}
