package com.ganziqim.core;

import com.ganziqim.utils.Dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by GanZiQim on 2016/11/22.
 */
public class SqliteDatabase extends Database {
    private String filePath;

    public SqliteDatabase(String filePath) {
        super();
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            conn.setAutoCommit(false);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }
}
