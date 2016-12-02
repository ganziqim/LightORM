package com.ganziqim.core;

import com.ganziqim.utils.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by ganzi on 2016/11/22.
 */
public class MysqlDatabase extends Database {
    private String databaseName;
    private String userName;
    private String password;

    public MysqlDatabase(String databaseName, String userName, String password) {
        super();
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Properties p = new Properties();
            p.setProperty("useSSL", "false");
            p.setProperty("user", userName);
            p.setProperty("password", password);
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + databaseName, p);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        connected = true;
        return conn;
    }
}
