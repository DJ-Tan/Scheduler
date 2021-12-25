package tempnus.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class TimetableGUI implements Initializable {

    @FXML
    protected WebView web = new WebView();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        web.getEngine().load("https://nusmods.com/timetable/sem-2/share?EG2401A=TUT:210,LEC:2&GES1041=LEC:1,TUT:D1&GET1029=TUT:W7,LEC:1&ME2102=LAB:6,PLEC:1,PTUT:T2C&ME2115=LEC:1,LAB:2A3,TUT:2N&ME2121=LAB:2B3,LEC:1,TUT:2F&PC2031=LEC:1");
        web.setZoom(0.83);
    }
}
