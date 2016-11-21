package com.ganziqim.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Session {
    ArrayList<String> savepoints = null;
    Connection con = null;
    Statement stmt = null;

    public Session() {
        savepoints = new ArrayList<String>();
        savepoints.add("default");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/student", "root", "root");
            stmt = con.createStatement();
            stmt.execute("START TRANSACTION");
            stmt.execute("SAVEPOINT " + savepoints.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addSavepoint(String newSavepoint) {
        if (savepoints.contains(newSavepoint)) {
            return false;
        } else {
            savepoints.add(newSavepoint);
            return true;
        }
    }

    public void rollbackAll() {
        try {
            stmt.execute("ROLLBACK");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        savepoints.clear();
    }

    public void rollback() {
        rollback(savepoints.get(savepoints.size() - 1));
    }

    public void rollback(String savepoint) {
        int index = savepoints.indexOf(savepoint);
        savepoints.subList(0, index);

        try {
            stmt.execute("ROLLBACK TO " + savepoint);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            stmt.execute("COMMIT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            con.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
