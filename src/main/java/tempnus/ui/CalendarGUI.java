package tempnus.ui;

import com.calendarfx.model.Calendar;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.RequestEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import org.apache.commons.lang.ObjectUtils;
import org.controlsfx.control.CheckListView;
import tempnus.logic.CommonControls;
import tempnus.logic.Timetable;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CalendarGUI extends CommonControls implements Initializable {

    @FXML
    protected CalendarView calendarView = new CalendarView();

    @FXML
    protected CheckListView calendarList = new CheckListView();

    @FXML
    protected TextField calendarName;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        calendarView.showWeekPage();
        calendarView.getYearPage().addEventHandler(RequestEvent.REQUEST_DATE, evt -> evt.consume());
        calendarView.getMonthPage().addEventHandler(RequestEvent.REQUEST_DATE, evt -> evt.consume());
        calendarView.getWeekPage().addEventHandler(RequestEvent.REQUEST_DATE, evt -> evt.consume());
        calendarList.setCellFactory(lv -> new CheckBoxListCell<Calendar>(calendarList::getItemBooleanProperty) {
            @Override
            public void updateItem(Calendar calendar, boolean empty) {
                super.updateItem(calendar, empty);
                setText(calendar == null ? "" : String.format(calendar.getName()));
            }
        });
    }

    @FXML
    protected TextField fileLoc;

    @FXML
    void fileSelector(ActionEvent event) {
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(window);
        try {
            String fileLocation = file.getAbsolutePath();
            fileLoc.setText(fileLocation);
        } catch (NullPointerException e) {

        }
    }

    @FXML
    protected void showWeekPage() {
        calendarView.showWeekPage();
    }

    @FXML
    protected void showYearPage() {
        calendarView.showYearPage();
    }
    @FXML
    protected void showMonthPage() {
        calendarView.showMonthPage();
    }

    @FXML
    protected void importCalendar() {
        Calendar calendar = new Calendar(calendarName.getText());
        calendarList.getItems().add(calendar);
    }
}