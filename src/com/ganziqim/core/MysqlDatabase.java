package com.ganziqim.core;

import com.ganziqim.utils.Dao;

import java.sql.DriverManager;

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
    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + databaseName, userName, password);
            con.setAutoCommit(false);
            dao = new Dao(con);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        connected = true;
        return true;
    }
}
