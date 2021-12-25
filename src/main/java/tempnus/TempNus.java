package tempnus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import java.io.IOException;

public class TempNus extends Application {

    private static final String APP_NAME = "Scheduler";
    private static final String LOGIN_VIEW = "/layout/timetable_view.fxml";
    private static final int APP_WIDTH = 600;
    private static final int APP_HEIGHT = 400;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TempNus.class.getResource(LOGIN_VIEW));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(APP_NAME);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        //grab your root here
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        //move around here
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    

}
