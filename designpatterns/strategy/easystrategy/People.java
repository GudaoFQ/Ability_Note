package desgindemo.stretagy.easystrategy;

/**
 * 需要进行比较的实体
 * Author : GuDao
 * 2020-10-14
 */
public class People {
    private String name;
    private Integer age;
    private Integer height;

    public String getName() {
        return name;
    }

    public People setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public People setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public People setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public People() {
    }

    public People(String name, Integer age, Integer height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                '}';
    }
}
