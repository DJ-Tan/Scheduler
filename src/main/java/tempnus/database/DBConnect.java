package tempnus.database;

import java.sql.*;

public class DBConnect {

    private static final String URL = "jdbc:postgresql://ec2-54-162-211-113.compute-1.amazonaws.com:5432/dh72nrvttlqg";
    private static final String USER = "zbnzkulsrarhta";
    private static final String PASSWORD = "1a9f5b22c25b541f29861237103ce39392dc3284f285d82301c3914c779d922d";

    /**
     * Executes a select query that returns values from the database.
     *
     * @param query Select query to be executed.
     * @return Result set of values from the query.
     * @throws SQLException If there is an error connecting to the database.
     */
    public static ResultSet selectQuery(String query) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        conn.close();
        return rs;
    }

    /**
     * Executes a query that modifies the database.
     *
     * @param query Modifying query to be executed.
     * @throws SQLException If there is an error connecting to the database.
     */
    public static void modifyQuery(String query) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        stmt.executeQuery(query);
        conn.close();
    }
}