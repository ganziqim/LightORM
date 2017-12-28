package Test;

import com.ganziqim.annotation.MaxLength;
import com.ganziqim.annotation.NotNull;
import com.ganziqim.annotation.PrimaryKey;

/**
 * Created by GanZiQim on 2016/10/27.
 */
public class Student {
    @PrimaryKey
    private int id;
    @MaxLength(30)
    private String name;
    private int age;
    private double tuition;
    @NotNull
    private int test1;

    public Student() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getTuition() {
        return tuition;
    }

    public void setTuition(double tuition) {
        this.tuition = tuition;
    }

    public int getTest1() {
        return test1;
    }

    public void setTest1(int test1) {
        this.test1 = test1;
    }
}
