package tempnus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TempNus extends Application {

    private static final String STARTUP_VIEW = "/layout/profile_view.fxml";
    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch();
    }

    /**
     * Initialises core settings for application upon startup.
     *
     * @param stage Container for all the objects of a JavaFX application.
     * @throws IOException If scene cannot be loaded with the fxml view.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TempNus.class.getResource(STARTUP_VIEW));
        Scene scene = new Scene(fxmlLoader.load());
        setStage(stage, scene);
        initMoveAppSettings(stage, scene);
    }

    /**
     * Initialises scene settings to drag around the application.
     *
     * @param stage Container for all the objects of a JavaFX application.
     * @param scene Container for the physical contents of a JavaFX application.
     */
    private void initMoveAppSettings(Stage stage, Scene scene) {
        // Updates x and y coordinates when mouse is pressed
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Moves stage based on updated x and y coordinates when cursor is dragged
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Initialises default settings for the stage.
     *
     * @param stage Container for all the objects of a JavaFX application.
     * @param scene Container for the physical contents of a JavaFX application.
     */
    private void setStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

}
