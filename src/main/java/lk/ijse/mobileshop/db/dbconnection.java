package lk.ijse.mobileshop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class dbconnection {
    private final static String URL = "jdbc:mysql://localhost:3306/mobileshop";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "12345");
    }

    private static dbconnection dbConnection;
    private Connection connection;

    private dbconnection() throws SQLException {
        connection = DriverManager.getConnection(URL, props);
    }

    public static dbconnection getInstance() throws SQLException {
        if(dbConnection == null) {
            return dbConnection = new dbconnection();
        } else {
            return dbConnection;
        }
    }
    public Connection getConnection() {
        return connection;
    }
}
