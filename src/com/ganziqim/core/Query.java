package com.ganziqim.core;

import com.ganziqim.utils.Dao;
import com.ganziqim.utils.MethodNameGetter;
import com.ganziqim.utils.SqlStringGenerator;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Query implements IQuery {
    private Class cls = null;
    private Session ses = null;
    private String sql = "";
    private Dao dao = null;

    // 有限状态自动机
    private static final int INIT = 0;
    private static final int UPDATE = 1;
    private static final int SELECT = 2;

    private int status = INIT;

    Query(Class cls, Session ses) {
        this.cls = cls;
        this.ses = ses;
        this.dao = new Dao(ses.getCon());
    }

    public void add(Object obj) {
        sql = "INSERT INTO ";
        String clsName = getClassName();

        sql += "`" + clsName + "` ";

        sql += columns() + " VALUES " + values(obj);

        System.out.println("trying " + sql);
        Statement stmt = null;
        try {
            stmt = ses.getCon().createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "";
    }

    public void addAll(List objs) {
        sql = "INSERT INTO ";
        String clsName = getClassName();

        sql += "`" + clsName + "` ";

        sql += columns() + " VALUES ";

        for (Object obj : objs) {
            sql += values(obj) + ",";
        }

        sql = sql.substring(0, sql.length()-1);

        System.out.println("trying " + sql);
        Statement stmt = null;
        try {
            stmt = ses.getCon().createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "";
    }

    private String values(Object obj) {
        String values = "(";

        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                values += SqlStringGenerator.getValueString(MethodNameGetter.getValue(obj, field)) + ",";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        values = values.substring(0, values.length()-1);
        values += ")";

        return values;
    }

    private String columns() {
        Field[] fields = cls.getDeclaredFields();

        String columns = "(";

        for (Field field : fields) {
            columns += "`" + field.getName() + "`,";
        }

        columns = columns.substring(0, columns.length()-1);
        columns += ")";

        return columns;
    }

    public IQuery delete() {
        if (status == INIT) {
            sql = "DELETE FROM " + getClassName();
            status = UPDATE;
        }
        return this;
    }

    public IQuery update(String exp) {
        if (status == INIT) {
            sql = "UPDATE " + getClassName() + " SET " + exp;
            status = UPDATE;
        }
        return this;
    }

    private String getClassName() {
        String[] objNames = cls.getName().split("\\.");
        return objNames[objNames.length - 1];
    }

    public IQuery where(String exp) {
        sql += " WHERE " + exp;
        if (status == UPDATE) {
            return this;
        } else if (status == SELECT) {
            return this;
        }
        return null;
    }

    public List execute() {
        System.out.println("trying " + sql);

        if (status == SELECT) {
            List<Map<String, Object>> result = dao.excuteQuery(sql);

            ArrayList lst = new ArrayList();
            try {
                for (Map<String, Object> map : result) {
                    Object obj = cls.newInstance();
                    Field[] fields = cls.getDeclaredFields();

//                    利用这个方法可以直接实例化对象，但是要求必须有一个全参数构造方法
//                    obj.getClass().getConstructor()

                    for (Field field : fields) {
                        MethodNameGetter.setValue(obj, field, map.get(field.getName().toLowerCase()));
                    }

                    lst.add(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return lst;
        } else {
            dao.executeUpdate(sql);
            sql = "";
            return null;
        }
    }

    public IQuery select() {
        status = SELECT;
        sql = "SELECT * FROM " + getClassName();
        return this;
    }

    public void all() {

    }

    public void limit() {

    }
}
