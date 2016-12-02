package com.ganziqim.core;

import com.ganziqim.annotation.MaxLength;
import com.ganziqim.annotation.PrimaryKey;
import com.ganziqim.utils.SqlStringGenerator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;

// LightORM
// create by GanZiQim at 2016.10.27
// 2016.10.27: createTable
class Database implements IDatabase {
    protected boolean connected = false;
    private int connectionNumbers = 5;
    private ArrayList<Session> sessionPool = null;

    public Database() {
        sessionPool = new ArrayList<Session>();
    }

    public Database(int ConnectionNumbers) {
        this.connectionNumbers = ConnectionNumbers;
    }

    public boolean init() {
        for (int i = 0; i < connectionNumbers; i++) {
            sessionPool.add(new Session(connect()));
        }
        return true;
    }

    public Connection connect() {
        return null;
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
                sessionPool.get(0).getDao().executeUpdate(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Session getSession() {
        for (Session ses : sessionPool) {
            if (!ses.isUsed()) {
                ses.setUsed(true);
                return ses;
            }
        }
        System.out.println("full!");
        return null;
    }

    public void dispose() {
        for (Session ses : sessionPool) {
            ses.dispose();
        }
        connected = false;
    }

    public void recovery(ISession session) {
        for (Session ses : sessionPool) {
            if (ses.equals(session)) {
                ses.setUsed(false);
            }
        }
    }
}
