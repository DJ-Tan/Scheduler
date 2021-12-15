package scheduler.test;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SchedulerController implements Initializable {

    @FXML
    private ListView<Task> taskList34 = new ListView<Task>();

    @FXML
    private ListView<Task> taskList35 = new ListView<Task>();
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskList35.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

            @Override
            public ListCell<Task> call(ListView<Task> param) {
                ListCell<Task> cell = new ListCell<Task>() {
                    @Override
                    public void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getTask());
                            setStyle("-fx-control-inner-background:  #cccccc;");
                        } else {
                            setText("");
                            setStyle("-fx-control-inner-background:  #cccccc;");
                        }
                    }
                };
                return cell;
            }
        });
    }

    @FXML
    private Label signUpText;

    @FXML
    private TextField signUpUser;

    @FXML
    private TextField signUpEmail;

    @FXML
    private PasswordField signUpPass;

    @FXML
    private PasswordField signUpRPass;

    @FXML
    private Button loginButton;

    public void onAddButtonClick() {
        taskList35.getItems().add(new Task("CFG1002", "Done", "01-Jan-2021", "02-Jan-2021", "VMock", ""));
    }

    public void onDelButtonClick() {
        taskList35.getItems().remove(taskList35.getSelectionModel().getSelectedItem());
    }

    public void onSignUpButtonClick() {
        try{
            if (isValidUserS(signUpUser.getText()) && isValidEmailS(signUpEmail.getText()) && (isValidPassS(signUpPass.getText(),signUpRPass.getText()))){
                signUpText.setTextFill(Paint.valueOf("Green"));
                signUpText.setText("Signed Up Successfully");
            }
        } catch (BadPasswordException e) {
            signUpText.setTextFill(Paint.valueOf("Red"));
            signUpText.setText(e.getMessage());
        }
    }

    private static boolean isValidUserS(String username) {
        if (username.isEmpty()) {
            throw new BadPasswordException("Username is not valid.");
        }
        return true;
    }

    private static boolean isValidEmailS(String email) {
        if (!Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches()){
            throw new BadPasswordException("Email is not valid.");
        }
        return true;
    }

    public static boolean isValidPassS(String password, String rPassword) throws BadPasswordException {
        if (!password.equals(rPassword)) {throw new BadPasswordException("Passwords do not match.");}
        if (!((password.length() >= 8) && (password.length() <= 15))) {throw new BadPasswordException("Password should be 8 to 15 characters long.");}
        if (password.contains(" ")) {throw new BadPasswordException("Password cannot contain blank spaces.");}
        if (true) {
            int count = 0;
            for (int i = 0; i <= 9; i++) {
                String str1 = Integer.toString(i);
                if (password.contains(str1)) {count = 1; break;}
            }
            if (count == 0) {throw new BadPasswordException("Password does not contain numbers.");}
        }
        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {throw new BadPasswordException("Password does not contain special characters.");}
        if (true) {
            int count = 0;
            for (int i = 65; i <= 90; i++) {
                char c = (char)i;
                String str1 = Character.toString(c);
                if (password.contains(str1)) {count = 1; break;}
            }
            if (count == 0) {throw new BadPasswordException("Password does not contain uppercase letters.");}
        }
        if (true) {
            int count = 0;
            for (int i = 90; i <= 122; i++) {
                char c = (char)i;
                String str1 = Character.toString(c);
                if (password.contains(str1)) {count = 1; break;}
            }
            if (count == 0) {throw new BadPasswordException("Password does not contain lowercase letters.");}
        }
        return true;
    }

    public void handleCancelButtonAction(ActionEvent event) {
        Button source = (Button) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void openProfile() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SchedulerApplication.class.getResource("profile-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage main = (Stage) loginButton.getScene().getWindow();
        main.setScene(scene);
    }
}