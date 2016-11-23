package Test;

import com.ganziqim.core.IDatabase;
import com.ganziqim.core.MysqlDatabase;
import com.ganziqim.core.Session;

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

        db.createTable(Student.class);

        Session s = db.getSession();

        Student stu = new Student();
        stu.setId(234);
        stu.setAge(12);
        stu.setName("jzs");
        s.add(stu);
        s.commit();

        s.dispose();
        db.dispose();
    }
}
