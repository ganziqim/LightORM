package com.ganziqim.core;

import com.ganziqim.annotation.MaxLength;
import com.ganziqim.annotation.PrimaryKey;
import com.ganziqim.utils.SqlStringGenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// LightORM
// create by GanZuQim at 2016.10.27
// 2016.10.27: createTable
class Database implements IDatabase {
    protected Connection con = null;
    protected Statement stmt = null;

    protected boolean connected = false;

    public boolean connect() {
        return false;
    }

    public void createTable(Class obj) {
        ArrayList<Class> objs = new ArrayList<Class>();
        objs.add(obj);
        createTable(objs);
    }

    public void createTable(ArrayList<Class> objs) {
        for (Class obj : objs) {
            Field[] fields;
            String[] objNames;

            fields = obj.getDeclaredFields();

            objNames = obj.getName().split("\\.");

            String objName = objNames[objNames.length - 1];

            String sql = "CREATE TABLE " + objName + "(";

            for (Field field : fields) {
                String sqlType = SqlStringGenerator.getSqlType(field);
                sql += field.getName() + " " + sqlType;

                if ("VARCHAR".equals(sqlType)) {
                    Annotation a = field.getAnnotation(MaxLength.class);
                    if (a != null) {
                        sql += "(" + field.getAnnotation(MaxLength.class).value() + ")";
                    }
                }

                if (field.getAnnotation(PrimaryKey.class) != null) {
                    sql += " PRIMARY KEY";
                }

                sql += ",";
            }
            sql = sql.substring(0, sql.length()-1);
            sql += ")";

            System.out.println("trying " + sql);

            try {
                stmt.executeUpdate(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Session getSession() {
        return new Session(con, stmt);
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
