package tempnus.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import tempnus.logic.CommonControls;
import javafx.embed.swing.SwingFXUtils;
import tempnus.logic.Timetable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TimetableGUI extends CommonControls implements Initializable {

    @FXML
    protected WebView web = new WebView();

    @FXML
    protected TextField timetableField;

    @FXML
    protected TextField semesterField;

    @FXML
    protected ImageView image;

    @FXML
    protected ListView<Timetable> timetableList = new ListView<Timetable>();

    public void loadTimetableLink() {
        String url = timetableField.getText();
        if (!(url.startsWith("https://") || url.startsWith("http://"))) {
            url = "https://" + url;
        }
        web.getEngine().load(url);
        web.setVisible(true);
        image.setVisible(false);
    }

    public void loadTimetable() {
        try {
            Timetable timetable = timetableList.getSelectionModel().getSelectedItem();
            image.setImage(new Image(timetable.getFile().toURI().toString()));
            web.setVisible(false);
            image.setVisible(true);
        } catch (Exception e) {
        }
    }

    public void deleteTimetable() {
        try {
            Timetable timetable = timetableList.getSelectionModel().getSelectedItem();
            timetableList.getItems().remove(timetable);
            timetable.getFile().delete();
            loadTimetable();
        } catch (Exception e) {
        }
    }

    public void screenshotTimetable() {
        String filename = semesterField.getText();
        ArrayList<String> timetableNames = new ArrayList<>();
        for (Timetable timetable: timetableList.getItems()){
            timetableNames.add(timetable.getSemester());
        }
        if (!filename.isBlank()) {
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            snapshotParameters.setTransform(Transform.scale(5.0,5.0));
            File captureFile = new File ( "src/main/resources/drawable/savedTimetables/" + filename + ".png");
            WritableImage image = web.snapshot(snapshotParameters, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write( bufferedImage, "png", captureFile);
                System.out.println("Captured WebView to: " + captureFile.getAbsoluteFile());
                if (timetableNames.contains(filename)) {
                    timetableList.getItems().remove(timetableList.getItems().remove(timetableNames.indexOf(filename)));
                }
                timetableList.getItems().add(new Timetable(filename,captureFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timetableList.setCellFactory(new Callback<ListView<Timetable>, ListCell<Timetable>>() {

            @Override
            public ListCell<Timetable> call(ListView<Timetable> param) {
                ListCell<Timetable> cell = new ListCell<Timetable>() {
                    @Override
                    public void updateItem(Timetable item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getSemester());
                            setFont(Font.font(20));
                        } else {
                            setText("");
                            setStyle("-fx-control-inner-background:  transparent;");
                            setFont(Font.font(20));
                        }
                    }
                };
                return cell;
            }
        });
        web.setZoom(0.83);
        web.setContextMenuEnabled(false);
        ObservableList<Timetable> timetables = FXCollections.observableArrayList();
        File folder = new File("src/main/resources/drawable/savedTimetables/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                timetables.add(new Timetable(file.getName().substring(0,file.getName().lastIndexOf(".")), new File("src/main/resources/drawable/savedTimetables/" + file.getName())));
            }
        }
        timetableList.setItems(timetables);
        timetableList.getSelectionModel().select(0);
        loadTimetable();
    }
}
