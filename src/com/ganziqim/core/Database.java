package com.ganziqim.core;

import com.ganziqim.annotation.MaxLength;
import com.ganziqim.annotation.NotNull;
import com.ganziqim.annotation.PrimaryKey;
import com.ganziqim.utils.SqlStringGenerator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

// LightORM
// create by GanZiQim at 2016.10.27
// 2016.10.27: createTable
class Database implements IDatabase {
    private boolean inited = false;
    private int connectionNumber = 0;
    private int initConnectionNumber = 5;
    private int maxConnectionNumber = 50;
    private int incrementalConnections = 5;
    private Vector<Session> sessionPool = null;

    protected String getTableColumnSqlScript = null;

    protected String databaseName = null;

    Database() {
        sessionPool = new Vector<Session>();
    }

    public int getInitConnectionNumber() {
        return initConnectionNumber;
    }

    public void setInitConnectionNumber(int initConnectionNumber) {
        this.initConnectionNumber = initConnectionNumber;
    }

    public int getMaxConnectionNumber() {
        return maxConnectionNumber;
    }

    public void setMaxConnectionNumber(int maxConnectionNumber) {
        this.maxConnectionNumber = maxConnectionNumber;
    }

    public int getIncrementalConnections() {
        return incrementalConnections;
    }

    public void setIncrementalConnections(int incrementalConnections) {
        this.incrementalConnections = incrementalConnections;
    }

    public boolean init() {
        addConnection(initConnectionNumber);
        inited = true;
        return true;
    }

    protected Connection getConnection() {
        return null;
    }

    private String getTableName(Class cls) {
        String[] clsNames = cls.getName().split("\\.");
        return clsNames[clsNames.length - 1];
    }

    public void createTable(List<Class> clazz) {
        for (Class cls : clazz) {
            createTable(cls);
        }
    }

    public void createTable(Class cls) {
        if (!inited) {
            System.out.println("Database uninit!");
            return;
        }

        Field[] fields = cls.getDeclaredFields();

        String tableName = getTableName(cls);

        String sql = "CREATE TABLE " + tableName + "(";

        for (Field field : fields) {
            String sqlType = SqlStringGenerator.getSqlType(field);
            sql += field.getName() + " " + sqlType;

            sql = fieldAnnotationHandler(field, sql);

            sql += ",";
        }

        sql = sql.substring(0, sql.length()-1);
        sql += ")";

        System.out.println("Trying " + sql);

        try {
            sessionPool.get(0).getDao().executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTable(Class cls) {
        if (!inited) {
            System.out.println("Database uninit!");
            return;
        }

        String tableName = getTableName(cls);

        Object[] params = new Object[2];
        params[0] = tableName;
        params[1] = databaseName;

        List<Map<String, Object>> result = null;

        result = sessionPool.get(0).getDao().executeQuery(getTableColumnSqlScript, params);
        ArrayList<String> sameColumns = new ArrayList<String>();

        Field[] fields = cls.getDeclaredFields();

        String sql = "ALTER TABLE " + tableName;

        for (Field field : fields) {
            int flag = 0;
            for (Map map : result) {
                if (field.getName().equals(map.get("COLUMN_NAME").toString())) {
                    sameColumns.add(field.getName());
                    result.remove(map);

                    flag = 1;
                    break;
                }
            }

            if (flag == 0) {
                String sqlType = SqlStringGenerator.getSqlType(field);
                sql += " ADD " + field.getName() + " " + sqlType;

                sql = fieldAnnotationHandler(field, sql);

                sql += ",";
            }
        }

        for (Map map : result) {
            sql += " DROP COLUMN " + map.get("COLUMN_NAME").toString() + ",";
        }

        sql = sql.substring(0, sql.length()-1);

        System.out.println("Trying " + sql);
        try {
            sessionPool.get(0).getDao().executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fieldAnnotationHandler(Field field, String sql) {
        String sqlType = SqlStringGenerator.getSqlType(field);

        if ("VARCHAR".equals(sqlType)) {
            Annotation a = field.getAnnotation(MaxLength.class);
            if (a != null) {
                sql += "(" + field.getAnnotation(MaxLength.class).value() + ")";
            } else {
                sql += "(128)";
            }
        }

        if (field.getAnnotation(NotNull.class) != null) {
            sql += " NOT NULL ";
        }

        if (field.getAnnotation(PrimaryKey.class) != null) {
            sql += " PRIMARY KEY";
        }

        return sql;
    }

    private void addConnection(int addNumber) {
        for (int i = 0; i < addNumber; i++) {
            sessionPool.addElement(new Session(getConnection()));
        }
        connectionNumber += addNumber;
    }

    public Session getSession() {
        if (!inited) {
            System.out.println("Database uninit!");
            return null;
        }

        for (Session ses : sessionPool) {
            if (!ses.isUsed()) {
                ses.setUsed(true);
                return ses;
            }
        }

        if (connectionNumber < maxConnectionNumber) {
            if (maxConnectionNumber - connectionNumber <= incrementalConnections) {
                addConnection(incrementalConnections);
            } else {
                addConnection(maxConnectionNumber - connectionNumber);
            }

            for (Session ses : sessionPool) {
                if (!ses.isUsed()) {
                    ses.setUsed(true);
                    return ses;
                }
            }
        }

        System.out.println("The connection pool is full!");
        return null;
    }

    public void dispose() {
        for (Session ses : sessionPool) {
            ses.dispose();
        }
        inited = false;
    }

    public void recovery(ISession session) {
        for (Session ses : sessionPool) {
            if (ses.equals(session)) {
                ses.setUsed(false);
            }
        }
    }
}
