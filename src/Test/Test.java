package Test;

import com.ganziqim.core.IDatabase;
import com.ganziqim.core.IQuery;
import com.ganziqim.core.MysqlDatabase;
import com.ganziqim.core.ISession;

import java.util.*;

/**
 * Created by admin on 2016/11/7.
 */
public class Test {
    public static void main(String[] args) {
        IDatabase db = new MysqlDatabase("lightorm", "root", "root");

        if (!db.connect()) {
            System.out.println("connect fail!");
            return;
        }
//
//        db.createTable(Student.class);

        ISession s = db.getSession();

//        Student stu = new Student();
//        stu.setId(1);
//        stu.setAge(12);
//        stu.setName("jzs");
//
//        Student stu2 = new Student();
//        stu2.setId(2);
//        stu2.setAge(22);
//        stu2.setName("haha");
//
//        ArrayList<Student> list = new ArrayList<Student>();
//        list.add(stu);
//        list.add(stu2);
//
//        s.getQuery(Student.class).addAll(list);
//        s.getQuery(Student.class).delete().where("id = 2").execute();
        s.getQuery(Student.class).update("name = \'heiheihei\'").execute();


//        List<Student> lst = s.getQuery(Student.class).select().execute();
//        for (Student stu : lst) {
//            System.out.println(stu.getId() + " " + stu.getName());
//        }

        s.dispose();
        db.dispose();
    }
}
