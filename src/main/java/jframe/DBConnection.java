package jframe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    //This allows a singleton databsase connections
    static Connection conn = null;

    public static Connection getConnection() {
        if(conn!=null) {
            return conn;
        }
        String sqlUrl = "jdbc:mysql://localhost/library_ms";
        String sqlUser = "devuser";
        String sqlPass = "toor";

        try {
            conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPass);
            System.out.println("Connection made");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
