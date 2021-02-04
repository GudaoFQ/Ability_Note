## Stream的使用

### 以前遍历的方式
```java
// 测试数组
String[] users = new String[]{"张三","李四","王二"};

// 以前的遍历方式
for(int i=0;i<users.length;i++){
    System.out.println(users[i]);
}

for(String u:users){
    System.out.println(u);
}
```

### 使用Stream遍历方式
```java
// 创建用户实体类
public class User {
    private Integer age;
    private String name;

    public User(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

// 使用stream进行数据遍历
public class StreamDemo {
    public static void main(String[] args) {
        List<User> list = new ArrayList<>();
        list.add(new User(11,"test"));
        list.add(new User(21,"gudao"));

        // 使用stream拉遍历数据信息
        list.stream().filter(user -> user.getAge()==21).forEach(user -> System.out.println(user));
    }
}
```