package com.ganziqim.core;

import java.sql.DriverManager;

/**
 * Created by ganzi on 2016/11/22.
 */
public class SqliteDatabase extends Database {
    private String filePath;

    public SqliteDatabase(String filePath) {
        super();
        this.filePath = filePath;
    }

    public String getFileName() {
        return filePath;
    }

    public void setFileName(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
