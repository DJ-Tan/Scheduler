package tempnus.ui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tempnus.database.PasswordHashing;
import tempnus.file.FileCreator;
import tempnus.file.FileReadWrite;
import tempnus.logic.CommonControls;
import tempnus.logic.user.LoginLogic;

import java.util.ArrayList;

public class LoginGUI extends CommonControls {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox rememberCheckBox;

    private static final String REMEMBER_DIR = "data";
    private static final String REMEMBER_FILEPATH = "data/credentials.txt";
    private static final String PROFILE_VIEW = "/layout/profile_view.fxml";
    private static final String REGISTER_VIEW = "/layout/register_view.fxml";
    private static final String FORGET_PASS_VIEW = "/layout/forget_pass_view.fxml";

    private boolean isRememberMe = false;

    /**
     * Execute default settings of the scene.
     */
    public void setupLoginGUI() {
        setPasswordTextField();
    }

    /**
     * Makes the password text field editable if a change in username is detected.
     */
    public void usernameFieldChangeEvent() {
        passwordTextField.setText("Password");
        passwordTextField.setEditable(true);
        isRememberMe = false;
    }

    /**
     * Checks user credentials and proceeds to log in to the user's account.
     *
     * @param event Base class for FX events.
     */
    public void login(Event event) {
        if (isValidCredentials()) {
            saveLoginDetails();
            SceneChanger.changeScene(event, PROFILE_VIEW);
        }
    }

    /**
     * Navigates to a registration page.
     *
     * @param event Base class for FX events.
     */
    public void register(Event event) {
        SceneChanger.changeScene(event, REGISTER_VIEW);
    }

    /**
     * Navigates to forget password / password retrieval scene.
     *
     * @param event Base class for FX events.
     */
    public void forgetPassword(Event event) {
        SceneChanger.changeScene(event, FORGET_PASS_VIEW);
    }

    /**
     * Checks the login credentials of the user.
     *
     * @return true if the username and password are valid; false if either or both are invalid.
     */
    private Boolean isValidCredentials() {
        ArrayList<String> loginDetails = getUsernamePassword();
        if (!LoginLogic.isValidUsername(loginDetails.get(0))) {
            errorLabel.setText("Username does not exist");
            errorLabel.setVisible(true);
            return false;
        } else if (!LoginLogic.isValidPassword(loginDetails.get(0), loginDetails.get(1), isRememberMe)) {
            errorLabel.setText("Invalid password");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     * Save the login details if the 'remember checkbox' is selected.
     */
    private void saveLoginDetails() {
        FileCreator.deleteAndRecreateFile(REMEMBER_DIR, REMEMBER_FILEPATH);
        ArrayList<String> loginDetails = new ArrayList<>();
        if (rememberCheckBox.isSelected()) {
            loginDetails.add("true");
            loginDetails.add(usernameTextField.getText());
            loginDetails.add(PasswordHashing.getHashValue(passwordTextField.getText()));
        } else {
            loginDetails.add("false");
        }
        FileReadWrite.writeToFile(loginDetails, REMEMBER_FILEPATH);
    }

    /**
     * Sets the password text field to uneditable if the 'remember checkbox' was selected before.
     */
    private void setPasswordTextField() {
        FileCreator.createFileIfNotExist(REMEMBER_DIR, REMEMBER_FILEPATH);
        ArrayList<String> loginDetails = FileReadWrite.readFromFile(REMEMBER_FILEPATH);
        if (loginDetails.get(0).equals("true")) {
            passwordTextField.setText("********");
            passwordTextField.setEditable(false);
            isRememberMe = true;
        }
    }

    /**
     * Get the username and password from the input or credentials file.
     *
     * @return Username and password.
     */
    private ArrayList<String> getUsernamePassword() {
        ArrayList<String> usernamePassword = new ArrayList<>();
        if (isRememberMe) {
            usernamePassword = FileReadWrite.readFromFile(REMEMBER_FILEPATH);
        } else {
            usernamePassword.add(usernameTextField.getText());
            usernamePassword.add(passwordTextField.getText());
        }
        return usernamePassword;
    }


}
