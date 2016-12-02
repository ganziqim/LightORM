package Test;

import com.ganziqim.core.*;

import java.util.*;

/**
 * Created by admin on 2016/11/7.
 */
public class Test {
    public static void main(String[] args) {
        //IDatabase db = new MysqlDatabase("lightorm", "root", "root");
        IDatabase db = new SqliteDatabase("student.db");

        if (!db.init()) {
            System.out.println("init fail!");
            return;
        }

        //db.createTable(Student.class);

        ISession s = db.getSession();

        Student stu = new Student();
        stu.setId(3);
        stu.setAge(45);
        stu.setName("sdf");

        Student stu2 = new Student();
        stu2.setId(4);
        stu2.setAge(30);
        stu2.setName("jia");

        ArrayList<Student> list = new ArrayList<Student>();
        list.add(stu);
        list.add(stu2);

        s.getQuery(Student.class).addAll(list);
//        s.getQuery(Student.class).delete().where("id = 2").execute();
//        s.getQuery(Student.class).update("name = \'heiheihei\'").execute();


//        List<Student> lst = s.getQuery(Student.class).select().execute();
//        for (Student stu : lst) {
//            System.out.println(stu.getId() + " " + stu.getName());
//        }

//        List<Student> stus = s.getQuery(Student.class).select().limit(1, 2).execute();
//        for (Student st : stus) {
//            System.out.println(st.getId() + " " + st.getName());
//        }

        s.commit();

        db.dispose();
    }
}
