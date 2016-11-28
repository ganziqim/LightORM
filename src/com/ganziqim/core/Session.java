package com.ganziqim.core;

import com.ganziqim.utils.Dao;
import com.ganziqim.utils.InstanceValueGetter;
import com.ganziqim.utils.SqlStringGenerator;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;

public class Session {
    Connection con = null;
    Dao dao = null;
    ArrayList<Savepoint> savepoints = null;

    public Session(Connection con) {
        this.con = con;

        savepoints = new ArrayList<Savepoint>();
        Savepoint initSavepoint = null;
        try {
            initSavepoint = con.setSavepoint("init");
            savepoints.add(initSavepoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dao = new Dao(con);
    }

    public boolean addSavepoint(String savepointName) {
        if (savepoints.contains(savepointName)) {
            return false;
        } else {
            try {
                savepoints.add(con.setSavepoint(savepointName));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public void rollback() {
        try {
            con.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rollback(String savepointName) {
        try {
            for (Savepoint savepoint : savepoints) {
                if (savepoint.getSavepointName() == savepointName) {
                    con.rollback(savepoint);
                    savepoints.remove(savepoint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            con.close();
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
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Query delete(Class cls) {
        return new Query(cls, this);
    }
}
