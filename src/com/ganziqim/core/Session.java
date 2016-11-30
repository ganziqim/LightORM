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

class Session implements ISession {
    private Connection con = null;
    private Dao dao = null;
    private ArrayList<Savepoint> savepoints = null;

    Session(Connection con) {
        this.con = con;

        savepoints = new ArrayList<Savepoint>();
        Savepoint initSavepoint = null;
        try {
            initSavepoint = con.setSavepoint("init");
            savepoints.add(initSavepoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Connection getCon() {
        return con;
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

    public Query getQuery(Class cls) {
        return new Query(cls, this);
    }
}
