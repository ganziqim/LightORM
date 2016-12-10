package com.ganziqim.utils;

import java.lang.reflect.Field;
import java.sql.JDBCType;

/**
 * Created by GanZiQim on 2016/11/23.
 */
public class SqlStringGenerator {
    public static String getSqlType(Field field) {
        return _getSqlType(field.getGenericType().toString());
    }

    public static String getSqlType(Object obj) {
        return _getSqlType(obj.getClass().getName());
    }

    private static String _getSqlType(String nameString) {
        System.out.println("getSqlType " + nameString);
        switch (nameString) {
            case "java.lang.Integer":
            case "int":
                return "INT";

            case "java.lang.String":
            case "class java.lang.String":
                return "VARCHAR";

            case "java.lang.Float":
            case "java.lang.Double":
            case "double":
            case "float":
                return "FLOAT";

            case "java.util.Date":
                return "DATE";
        }

        return null;
    }

    public static String getValueString(Object obj) {
        switch (obj.getClass().getName()) {
            case "java.lang.Integer":
            case "int":
                return obj.toString();

            case "java.lang.String":
            case "class java.lang.String":
                return "\'" + obj.toString() + "\'";

            case "java.lang.Float":
            case "java.lang.Double":
                return obj.toString();

            case "java.util.Date":
                return obj.toString();
        }

        return null;
    }

    public static void main(String[] args) {
        Math.random();
//        String a = "sdf";
//        System.out.println(getValueString(a));
    }
}
