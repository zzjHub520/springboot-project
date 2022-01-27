# SpringBoot开发非Web程序



SpringBoot开发非Web程序

在SpringBoot框架中，要创建一个非Web应用程序（纯Java）程序，新建一个SpringBoot项目，自动依赖的jar包

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
 
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```



可以看出不是spring-boot-starter-web的启动依赖包，Springboot框架，自动默认帮我们添加了开发Java项目的spring-boot-starter启动依赖包。

UserService接口

```java
public interface UserService {
    String sayHi(String name);
}
```

UserServiceImpl

```java
@Component
public class UserServiceImpl implements UserService {
    @Override
    public String sayHi(String name) {
        System.out.println("hi" +name);
        //访问dao层
        return name;
    }
}
```

```
@Component和@Service都可以
```

运行。

```java
@SpringBootApplication
public class SpringbootJavaApplication {

    public static void main(String[] args) {
        //SpringApplication.run()返回Spring容器对象
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootJavaApplication.class, args);
        UserService userService = (UserService) context.getBean("userServiceImpl");
        String name = userService.sayHi("九月");
        System.out.println(name);
    }

}
```


另一种方式运行。

```java
@SpringBootApplication
public class SpringbootJavaApplication implements CommandLineRunner {
    @Autowired
    private UserService userService;

    /**
     *  相当与Java 程序的 main方法
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        userService.sayHi("jiuyue");
    }
    public static void main(String[] args) {
        //启动Springboot,启动Spring容器
        SpringApplication.run(SpringbootJavaApplication.class, args);
     
    }

}
```



需要实现implements CommandLineRunner并实现接口的方法，public void run相当与Java程序的main方法。在SpringBoot项目中，还需要SpringBoot启动的main方法，通过这个main方法，启动SpringBoot,启动Spring容器,才能扫描注册Bean。