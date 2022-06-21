# springboot将http改造成https

https://blog.csdn.net/weixin_49100429/article/details/119206626?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~aggregatepage~first_rank_ecpm_v1~rank_v31_ecpm-2-119206626-null-null.pc_agg_new_rank&utm_term=springboot+%E5%B0%86HTTP%E8%AF%B7%E6%B1%82%E6%94%B9%E4%B8%BAhttps&spm=1000.2123.3001.4430

springboot的项目天然的就是http的，但是有时候客户觉得http不安全，想要使用https的请求访问怎么处理？话不多少，上重点，https是对http进行ssl加密的一种协议，简单来说就是更安全，那么要怎么做呢？首先要生成证书，这里有两种方式，一种上百度云弄一个证书，另一种是自己生成证书，利用jdk的命令生成证书

生成命令：keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650

关键字解释：

- alias  密钥别名 
- storetype 指定密钥仓库类型
- keyalg 生证书的算法名称，RSA是一种非对称加密算法
- keysize 证书大小
- keystore 生成的证书文件的存储路径
- validity 证书的有效期



1.在本地任意一位置建立一个文件夹，然后进入命令行模式,输入指令，然后填写以下内容

**密钥库口令**：证书密码，在后面的项目中配置证书时用到
**姓氏**：一般没什么用，在浏览器中查看证书时会显示，用于正式场合的证书还是需要填写标准。
**组织单位名称**：证书使用单位名称，一般没什么用，在浏览器中查看证书时会显示，用于正式场合的证书还是需要填写标准。
**所在的城市或区域名称**:浏览器中查看证书信息时会显示。
**所在的省/市/自治区名称**：浏览器中查看证书信息时会显示。
**单位的双字母国家/地区代码**：国家或地区编码，浏览器中查看证书信息时会显示。

这里我就不一一展示了



![img](MarkDownImages/springboot%E5%B0%86http%E6%94%B9%E9%80%A0%E6%88%90https.assets/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80OTEwMDQyOQ==,size_16,color_FFFFFF,t_70.png)

![image-20220621234243643](MarkDownImages/springboot%E5%B0%86http%E6%94%B9%E9%80%A0%E6%88%90https.assets/image-20220621234243643.png)

验证正确填是则会在该目录下生成一个证书文件，填否会重新填一遍，将该证书文件拷贝到项目的类路径下，即resources目录下

![img](MarkDownImages/springboot%E5%B0%86http%E6%94%B9%E9%80%A0%E6%88%90https.assets/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80OTEwMDQyOQ==,size_16,color_FFFFFF,t_70-16558255128692.png)

 然后在配置文件中加入以下配置

```
server.ssl.enabled=true  #开启ssl验证
server.ssl.key-store=classpath:keystore.p12 #证书文件位置
server.ssl.key-store-password=123456  #上面的密钥口令
server.ssl.key-store-type=PKCS12   #storetype 上面的类型
server.ssl.key-alias=tomcat    #tomcat上面的alias  别名
```

然后重启项目，就成了https的请求了,用http访问变成了这样

![img](MarkDownImages/springboot%E5%B0%86http%E6%94%B9%E9%80%A0%E6%88%90https.assets/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80OTEwMDQyOQ==,size_16,color_FFFFFF,t_70-165582579483910.png)

 用https,点继续成功访问

![img](MarkDownImages/springboot%E5%B0%86http%E6%94%B9%E9%80%A0%E6%88%90https.assets/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80OTEwMDQyOQ==,size_16,color_FFFFFF,t_70-165582586219918.png)