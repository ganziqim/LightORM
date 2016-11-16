package Main;

import java.lang.reflect.Field;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

// LightORM
// create by GanZuQim at 2016.10.27
// 2016.10.27: createTable
public class Program {
    public static void main(String[] args) {
        Student stu = new Student();

        ArrayList<String> objs = new ArrayList<String>();

        objs.add("Student");

        createTable(objs);
    }

    public static void createTable(String obj) {
        ArrayList<String> objs = new ArrayList<String>();
        objs.add(obj);
        createTable(objs);
    }

    public static void createTable(ArrayList<String> objs) {
        for (String obj : objs) {
            Field[] fields;
            String[] objNames;
            try {
                fields = Class.forName(obj).getFields();

                objNames = Class.forName(obj).getName().split("\\.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }

            String objName = objNames[objNames.length - 1];

            String sql = "CREATE TABLE " + objName + "(";

            for (Field field : fields) {
                String fieldType = "";

                switch (field.getGenericType().toString()) {
                    case "int": fieldType = "INT";break;
                    case "class java.lang.String": fieldType = "VARCHAR(30)";break;
                }

                sql += field.getName() + " " + fieldType;

                if (field.getName() == "id") {
                    sql += " " + "PRIMARY KEY";
                }

                sql += ",";
            }
            sql = sql.substring(0, sql.length()-1);
            sql += ")";

            System.out.println("trying " + sql);

            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/student", "root", "root");

                Statement stmt;
                stmt = con.createStatement();

                stmt.executeUpdate(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
