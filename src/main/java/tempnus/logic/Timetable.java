package tempnus.logic;

import javafx.scene.image.Image;

import java.io.File;

public class Timetable {

    private String semester;
    private File file;

    public Timetable(String semester, File fileLink) {
        this.semester = semester;
        this.file = fileLink;
    }

    public String getSemester() {
        return this.semester;
    }

    public File getFile() {
        return this.file;
    }
}
