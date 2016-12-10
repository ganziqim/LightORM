package com.ganziqim.core;

import com.ganziqim.utils.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by GanZiQim on 2016/11/22.
 */
public class MysqlDatabase extends Database {
    private String userName;
    private String password;

    public MysqlDatabase(String databaseName, String userName, String password) {
        super();
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        getTableColumnSqlScript = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=? AND TABLE_SCHEMA=?";
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties p = new Properties();
            p.setProperty("useSSL", "false");
            p.setProperty("user", userName);
            p.setProperty("password", password);
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + databaseName, p);
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }
}
