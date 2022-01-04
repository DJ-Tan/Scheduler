package tempnus.logic.user;

import tempnus.database.DBConnect;
import tempnus.database.PasswordHashing;
import tempnus.logger.AppLogger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class LoginLogic {

    private static final String VALID_USER_QUERY = "SELECT * FROM Users WHERE uname = '%1$s'";
    private static final String VALID_PASSWORD_QUERY = "SELECT password FROM Users WHERE uname = '%1$s";

    /**
     * Checks if the specified username exists within the database.
     *
     * @param username Login username.
     * @return true if the username exists; false if the username does not exist.
     */
    public static Boolean isValidUsername(String username) {
        String query = String.format(VALID_USER_QUERY, username);
        ResultSet rs = DBConnect.selectQuery(query);
        Boolean isExist = null;
        try {
            isExist = rs.next();
        } catch (SQLException e) {
            AppLogger.log(Level.WARNING, e.getMessage());
        }
        return isExist;
    }

    /**
     * Checks if the specified password is correct.
     *
     * @param username Login username.
     * @param password Login password.
     * @param isHashed true if the password is already hashed, false if the password is not hashed.
     * @return true if the password is correct; false if the password is incorrect.
     */
    public static Boolean isValidPassword(String username, String password, boolean isHashed) {
        String query = String.format(VALID_PASSWORD_QUERY, username);
        ResultSet rs = DBConnect.selectQuery(query);
        String passwordDB = null;
        try {
            passwordDB = rs.getString("password");
        } catch (SQLException e) {
            AppLogger.log(Level.WARNING, e.getMessage());
        }
        String passwordHashed;
        if (isHashed) {
            passwordHashed = password;
        } else {
            passwordHashed = PasswordHashing.getHashValue(password);
        }
        assert passwordDB != null;
        return passwordDB.equals(passwordHashed);
    }

}
