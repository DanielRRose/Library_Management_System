package jframe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static Connection conn = null;

    public static Connection getConnection() {

        String sqlUrl = "jdbc:mysql://localhost/library_ms";
        String sqlUser = "devuser";
        String sqlPass = "toor";

        try {
            Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPass);
            if (conn != null) {
                System.out.println("Connection made");
                return conn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
