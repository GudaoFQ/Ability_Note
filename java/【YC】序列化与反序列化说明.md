## 序列化与反序列化说明

### 序列化本质
> 序列化是指把一个Java对象变成二进制内容，本质上就是一个byte[]数组

### 为什么需要序列化
> 序列化后可以把`byte[]`保存到文件中，或者把`byte[]`通过网络传输到远程，这样，就相当于把Java对象存储到文件或者通过网络传输出去了。 

## 反序列化
> 反序列是把一个二进制内容（也就是byte[]数组）变回Java对象。

### 为什么需要反序列化 
> 保存到文件中的`byte[]`数组又可以“变回”Java对象，或者从网络上读取byte[]并把它“变回”Java对象。

### Java对象的序列化
* Java平台允许我们在内存中创建可复用的Java对象，但一般情况下，只有当JVM处于运行时，这些对象才可能存在，即，这些对象的生命周期不会比JVM的生命周期更长。但在现实应用中，就可能**要求在JVM停止运行之后能够保存(持久化)指定的对象，并在将来重新读取被保存的对象**。Java对象序列化就能够帮助我们实现该功能。
* 使用Java对象序列化，在保存对象时，会把其状态保存为一组字节，在未来，再将这些字节组装成对象。必须注意地是，对象序列化保存的是对象的”状态”，即它的成员变量。由此可知，对象序列化**不会关注类中的静态变量和transient修饰的属性**。
* 除了在持久化对象时会用到对象序列化之外，当使用RMI(远程方法调用)，或在网络中传递对象时，都会用到对象序列化。Java序列化API为处理对象序列化提供了一个标准机制。

### 序列化及反序列化相关知识
* 在Java中，只要一个类实现了java.io.Serializable接口，那么它就可以被序列化。
* 通过ObjectOutputStream和ObjectInputStream对对象进行序列化及反序列化
* 虚拟机是否允许反序列化，不仅取决于类路径和功能代码是否一致，一个非常重要的一点是两个类的序列化 ID 是否一致（就是 private static final long serialVersionUID）
* 序列化并不保存静态变量。
* 要想将父类对象也序列化，就需要让父类也实现Serializable 接口。
* Transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null。
* 服务器端给客户端发送序列化对象数据，对象中有一些数据是敏感的，比如密码字符串等，希望对该密码字段在序列化时，进行加密，而客户端如果拥有解密的密钥，只有在客户端进行反序列化时，才可以对密码进行读取，这样可以一定程度保证序列化对象的数据安全。

### 序列化与反序列化本地存储Demo
#### 实体
```java
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
    private static final long serialVersionUID = 8294180014912103005L;

    public static String username;
    private transient String passwd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
```
#### 测试案例
```java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TransientTest {
    public static void main(String[] args) {
        // 赋值
        User user = new User();
        user.setUsername("Gudao");
        user.setPasswd("123456");
        // 打印信息
        System.out.println("read before Serializable: ");
        System.out.println("username: " + user.getUsername());
        System.err.println("password: " + user.getPasswd());

        ObjectOutputStream os = null;
        ObjectInputStream is = null;
        // 将序列化对象存储到本地
        try {
            os = new ObjectOutputStream(new FileOutputStream("D:/user.txt"));
            os.writeObject(user); // 将User对象写进文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // 在反序列化之前改变username的值
            User.username = "Test";
            // 读取本地的序列化对象
            is = new ObjectInputStream(new FileInputStream("D:/user.txt"));
            user = (User) is.readObject(); // 从流中读取User的数据
            // 打印反序列化信息
            System.out.println("\nread after Serializable: ");
            System.out.println("username: " + user.getUsername());
            System.err.println("password: " + user.getPasswd());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```
#### 本地user.txt文件
```text
 sr 8com.wedding.project.configuration.generator.comment.Users厍s]  xp
```

#### 运行结果
```shell
read before Serializable: 
username: Gudao
password: 123456

read after Serializable: 
username: Test
password: null
```