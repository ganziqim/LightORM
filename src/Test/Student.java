package Test;

import com.ganziqim.annotation.MaxLength;
import com.ganziqim.annotation.PrimaryKey;

/**
 * Created by admin on 2016/10/27.
 */
public class Student {
    @PrimaryKey
    private int id;
    @MaxLength(30)
    private String name;
    private int age;

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
}
