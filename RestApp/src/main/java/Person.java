import javax.xml.bind.annotation.XmlRootElement;
import javax.*;
@XmlRootElement (name="person")
public class Person {
    private int id;
    private int age;
    private String name;

    public Person(){}

    public Person(int id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return id+"::"+name+"::"+age;
    }

}