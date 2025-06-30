/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.io.FileInputStream;
import java.util.Properties;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author nyoma
 */
public class DBConnection {
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            try
            {
                Properties prop = new Properties();
                prop.load(new FileInputStream("config.properties"));
                
                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.user");
                String password = prop.getProperty("db.password");
                
                conn = DriverManager.getConnection(url, user, password);
            } catch (Exception e)
            {
                
                e.printStackTrace();
                return null;
            }
        }
        return conn;
    }
}
