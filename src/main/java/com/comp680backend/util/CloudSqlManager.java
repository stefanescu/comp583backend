package com.comp680backend.util;

import com.google.apphosting.api.ApiProxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class CloudSqlManager {
    private Connection conn;
    private void initConnection() {
        try {
            ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
            Map<String,Object> attr = env.getAttributes();
            String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");

            String url = hostname.contains("localhost:")
                    ? System.getProperty("cloudsql-local") : System.getProperty("cloudsql");
            System.out.println("connecting to: " + url);
            try {
                conn = DriverManager.getConnection(url);
                initDBSchema();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } finally {
            // Nothing really to do here.
        }
    }

    private void initDBSchema() {
        String createUserTable = "CREATE TABLE IF NOT EXISTS users(" +
                "id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
                "username VARCHAR(100) NULL," +
                "email VARCHAR(100) NULL," +
                "PRIMARY KEY (id));";

        String createGamesTable = "CREATE TABLE IF NOT EXISTS games(" +
                " id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
                " name VARCHAR(255) NOT NULL," +
                " release_date TIMESTAMP NULL," +
                " publisher VARCHAR(45) NULL," +
                " PRIMARY KEY (id));";

        String createScoresTable = "CREATE TABLE IF NOT EXISTS scores (" +
                "  id INT UNSIGNED NOT NULL AUTO_INCREMENT," +
                "  score INT NOT NULL," +
                "  image_url VARCHAR(255) NOT NULL," +
                "  games_id INT UNSIGNED NOT NULL," +
                "  users_id INT UNSIGNED NOT NULL," +
                "  PRIMARY KEY (id));";

//        String createTestUserData = "INSERT INTO users (username, email) VALUES (?,?);";
//        User user = new User();
//        user.setUserName("tester");
//        user.setEmail("test@test.com");
        try {
            conn.createStatement().executeUpdate(createUserTable);
            conn.createStatement().executeUpdate(createGamesTable);
            conn.createStatement().executeUpdate(createScoresTable);
//            PreparedStatement preparedStatement = conn.prepareStatement(createTestUserData);
//            preparedStatement.setString(1, user.getUserName());
//            preparedStatement.setString(2, user.getEmail());
//            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    private static CloudSqlManager instance;
    public static CloudSqlManager getInstance() {
        if(instance == null) {
            instance = new CloudSqlManager();
            instance.initConnection();
        }
        return instance;
    }
}
