package tempnus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TempNus extends Application {

    private static final String APP_NAME = "Scheduler";
    private static final String LOGIN_VIEW = "/layout/login_view.fxml";
    private static final int APP_WIDTH = 600;
    private static final int APP_HEIGHT = 400;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TempNus.class.getResource(LOGIN_VIEW));
        Scene scene = new Scene(fxmlLoader.load(), APP_WIDTH, APP_HEIGHT);
        stage.setTitle(APP_NAME);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    

}
