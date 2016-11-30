package com.ganziqim.core;

import com.ganziqim.utils.Dao;
import com.ganziqim.utils.InstanceValueGetter;
import com.ganziqim.utils.SqlStringGenerator;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;

class Query implements IQuery {
    private Class cls = null;
    private Session ses = null;
    private String sql = "";
    private Dao dao = null;

    Query(Class cls, Session ses) {
        this.cls = cls;
        this.ses = ses;
        this.dao = new Dao(ses.getCon());
    }

    public void add(Object obj) {
        sql = "INSERT INTO ";
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
            stmt = ses.getCon().createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "";
    }

    public IQuery delete() {
        sql = "DELETE FROM " + getClassName();
        return this;
    }

    private String getClassName() {
        String[] objNames = cls.getName().split("\\.");
        return objNames[objNames.length - 1];
    }

    public IQuery where(String exp) {
        sql += " WHERE " + exp;
        return this;
    }

    public void execute() {
        System.out.println("trying " + sql);
        dao.executeUpdate(sql);
    }

    public void all() {

    }

    public void first() {

    }

    public void last() {

    }
}
