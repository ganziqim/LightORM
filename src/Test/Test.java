package Test;

import java.util.ArrayList;
import com.ganziqim.core.Program;

/**
 * Created by admin on 2016/11/7.
 */
public class Test {
    public static void main(String[] args) {
        Student stu = new Student();

        ArrayList<String> objs = new ArrayList<String>();

        objs.add("Student");

        Program.createTable(objs);
    }
}
