package scheduler.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.regex.Pattern;

public class SchedulerController {

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
    private Button closeButton;

    @FXML
    private Button helloButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        helloButton.setVisible(false);
        closeButton.setVisible(true);
    }

    @FXML
    protected void onSignUpButtonClick() {
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

    @FXML
    private static boolean isValidUserS(String username) {
        if (username.isEmpty()) {
            throw new BadPasswordException("Username is not valid.");
        }
        return true;
    }

    @FXML
    private static boolean isValidEmailS(String email) {
        if (!Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches()){
            throw new BadPasswordException("Email is not valid.");
        }
        return true;
    }

    @FXML
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

    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        Button source = (Button) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openProfile() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SchedulerApplication.class.getResource("profile-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        Stage main = (Stage) loginButton.getScene().getWindow();
        main.setScene(scene);
    }
}