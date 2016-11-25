package com.ganziqim.core;

import com.ganziqim.utils.InstanceValueGetter;
import com.ganziqim.utils.SqlStringGenerator;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

class Session {
    ArrayList<String> savepoints = null;
    Connection con = null;
    Statement stmt = null;

    public Session(Connection con, Statement stmt) {
        savepoints = new ArrayList<String>();
        savepoints.add("default1");

        this.con = con;
        try {
            this.stmt = con.createStatement();
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
        System.out.println("trying commit");
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

    public void add(Object obj) {
        String sql = "INSERT INTO ";
        String[] objNames = obj.getClass().getName().split("\\.");
        String objName = objNames[objNames.length - 1];

        sql += objName + " ";

        Field[] fields = obj.getClass().getDeclaredFields();

        String columns = "(";
        String values = "(";

        for (Field field : fields) {
            columns += field.getName() + ",";

            try {
                values += SqlStringGenerator.getValueString(InstanceValueGetter.getValue(obj, field)) + ",";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        columns = columns.substring(0, columns.length()-1);
        values = values.substring(0, values.length()-1);

        columns += ")";
        values += ")";

        sql += columns + " VALUES " + values;

        System.out.println("trying " + sql);
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        String sql = "DELETE FROM ";

    }
}
