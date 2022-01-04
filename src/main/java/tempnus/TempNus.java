package tempnus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tempnus.logic.SQL;

import java.io.IOException;

public class TempNus extends Application {

    private static final String LOGIN_VIEW = "/layout/calendar_view.fxml";
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TempNus.class.getResource(LOGIN_VIEW));
        Scene scene = new Scene(fxmlLoader.load());
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
        SQL conn = new SQL("org.postgresql.Driver","jdbc:postgresql://ec2-54-162-211-113.compute-1.amazonaws.com:5432/dh72nrvttlqg","zbnzkulsrarhta","1a9f5b22c25b541f29861237103ce39392dc3284f285d82301c3914c779d922d");
        conn.getConnection();
        launch();
    }

    

}
