package tempnus.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tempnus.logic.CommonControls;

public class LoginGUI extends CommonControls {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox rememberCheckBox;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button forgetButton;

//    public void login() {
//        checkCredentials();
//
//    }

    /**
     *
     */
//    private void checkCredentials() {
//        if (!isValidUsername() || !isValidPassword) {
//            errorLabel.setText("Invalid username or password");
//            errorLabel.setVisible(true);
//        }
//    }

}
