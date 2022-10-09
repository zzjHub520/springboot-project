# [崛起于 Springboot2.X + 多模块整合 + 一个启动类 + jar、war 打包运行（60）](https://my.oschina.net/mdxlcj/blog/3134501)



博客概要：Springboot 多模块项目搭建 + 打包 jar 运行 + 打包 war 运行 + 一个启动类（有的项目是多模块多个启动类），为后续的 SpringCloud 项目作准备。

```
父工程：father

子模块：sun1、sun2、web（启动模块）
```



#### 1、创建父工程

   勾选 SpringWeb 和 lombok



如图所示



然后删除 src 文件夹，如图：



然后修改 pom 文件，添加一行打包类型

```
<packaging>pom</packaging>
```



#### 2、创建子模块

   创建 3 个子模块，分别为 sun1、sun2、web，不勾选任何依赖。





#### 3、father 的 pom.xml 修改

   创建好三个模块之后，在 father 项目父工程 pom.xml 添加依赖

```
<modules>
    <module>sun1</module>
    <module>sun2</module>
    <module>web</module>
</modules>
```

   更换 pom.xml 插件，准备打包使用

```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.19.1</version>
            <configuration>
                <skipTests>true</skipTests>    <!--默认关掉单元测试 -->
            </configuration>
        </plugin>
    </plugins>
</build>
```



#### 4、子模块 pom.xml 修改

   更换继承：sun1、sun2、web 三个子模块分别继承 father 父工程，这样就同样拥有了 lombok 以及 web 的依赖

```
<parent>
    <groupId>com.osc</groupId>
    <artifactId>father</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</parent>
```

   删除：因为 web 是启动类项目，不需要所以相关的打包插件，但是 sun1、sun2 需要删除下面的，因为是多余的，如下：

```
<build>
     <plugins> 
        <plugin>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-maven-plugin</artifactId>
         </plugin>
     </plugins>
</build>
```

​    删除：删除共同的配置

```
<properties> 
    <java.version>1.8</java.version> 
</properties>
```

   添加：打包类型

```
<packaging>jar</packaging>
```



#### 5、编码

sun1 项目上创建 entity 层，这个时候就可以使用 lombok 插件了，因为继承了父工程



```
@Data
@AllArgsConstructor
@ToString
public class UserEntity {
    private String name;
    private int age;
    private String address;
}
```

sun2 定义为 service 层，需要使用 sun1 的实体类，然后依赖 sun1，在 sun2 的 pom.xml 中配置

```
<dependency>
    <groupId>com.osc</groupId>
    <artifactId>sun1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

编写 service



```
@Service
public class HelloService {
    public UserEntity getUser(){
        return new UserEntity("jiutian",23,"beijing");
    }
}
```

这个时候我们就是引用 sun1 的实体类

   最后我们创建 web 层，这个不需要删除启动类，然后都依赖 sun1，sun2，把他们引入到 pom.xml 文件中，添加 sun1，sun2 依赖。

```
<dependency>
    <groupId>com.osc</groupId>
    <artifactId>sun1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.osc</groupId>
    <artifactId>sun2</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

最后在启动类上添加扫描，因为多个模块，web 启动类运行的话默认只运行扫描自己这个模块，导致找不到其他模块，所以添加一个扫描。

```
@ComponentScan(basePackages = {"com.osc.sun2.service","com.osc.web.controller"})
```

然后 web 项目上创建一个 controller 层



```
@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping(value = "/test")
    public String test(){
        return helloService.getUser().toString();
    }
}
```



#### 6、IDEA 运行、测试





#### 7、jar 打包

   在 web 项目 pom.xml 中插件更换为

```
<build>
    <!--打包之后的名字-->
    <finalName>demo_many</finalName>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.osc.web.WebApplication</mainClass>
                <layout>ZIP</layout>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                    <!-- &lt;!&ndash;可以生成不含依赖包的不可执行Jar包&ndash;&gt;
                     <configuration>
                         <classifier>exec</classifier>
                     </configuration>-->
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

   然后使用 IDEA 右侧 maven，当然，这个 maven 一定是 root 的，也就是父工程下面才可以，如下：



然后 jar 包打包成功，如图：



然后 jar 包就此成功，然后我们去命令行启动



这个时候启动了，我们继续刚刚访问，



   这个和本地测试一样，说明 jar 包成功。



#### 8、war 包

将 web 项目的 pom 文件

```
<packaging>war</packaging>
```

然后删掉指向类配置，剩下的如下

```
<plugins>
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <executions>
            <execution>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
```

如图：



关掉之前启动的 jar 命令，用 kill -9 关掉，然后启动 war，如图：



接下来，继续测试，



还是之前的结果，说明无论是我们在 IDEA 上测试，还是通过打包 jar 或者 war，都可以得到相同结果，这就说明我们的多模块项目单启动类配置完成了。

注意：并不是只有 web 项目才可以写 controller 层，如果你再创建一个模块，把它依赖引入，然后在启动类上添加扫描位置，其他子模块的 controller 也是可以执行的。一般情况下我们企业做项目也往往是这样子的，一个模块的 controller 层用来后台管理系统的，另外一个模块的 controller 层用来前台系统的。