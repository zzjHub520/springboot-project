# Spring Boot整合FTPClient线程池的实现示例

最近在写一个FTP上传工具，用到了Apache的FTPClient，但是每个线程频繁的创建和销毁FTPClient对象对服务器的压力很大，因此，此处最好使用一个FTPClient连接池。仔细翻了一下Apache的api，发现它并没有一个FTPClientPool的实现，所以，不得不自己写一个FTPClientPool。下面就大体介绍一下开发连接池的整个过程，供大家参考。

我们可以利用Apache提供的common-pool包来协助我们开发连接池。而开发一个简单的对象池，仅需要实现common-pool 包中的ObjectPool和PoolableObjectFactory两个接口即可。

**线程池的意义**

为了减少频繁创建、销毁对象带来的性能消耗，我们可以利用对象池的技术来实现对象的复用。对象池提供了一种机制，它可以管理对象池中对象的生命周期，提供了获取和释放对象的方法，可以让客户端很方便的使用对象池中的对象。

**pom引入依赖**

```xml
<!-- FtpClient依赖包-->
   <dependency>
     <groupId>commons-net</groupId>
     <artifactId>commons-net</artifactId>
     <version>3.5</version>
   </dependency>
 
   <!-- 线程池-->
   <dependency>
     <groupId>commons-pool</groupId>
     <artifactId>commons-pool</artifactId>
     <version>1.6</version>
   </dependency>
 
   <dependency>
     <groupId>org.apache.commons</groupId>
     <artifactId>commons-pool2</artifactId>
     <version>2.0</version>
   </dependency>
```

**创建ftp配置信息**

在resources目录下创建ftp.properties配置文件,目录结构如下：

![img](MarkDownImages/ftp.assets/20181223170638930-16576460658474.png)

添加如下的配置信息:

```properties
########### FTP用户名称 ###########
ftp.userName=hrabbit
########### FTP用户密码 ###########
ftp.passWord=123456
########### FTP主机IP ###########
ftp.host=127.0.0.1
########### FTP主机端口号 ###########
ftp.port=21
########### 保存根路径 ###########
ftp.baseUrl=/
```

**创建FTPProperties.java配置文件**

加载配置内容到Spring中,配置信息基本延用我的就可以。

```java
/**
 * FTP的配置信息
 * @Auther: hrabbit
 * @Date: 2018-12-03 2:06 PM
 * @Description:
 */
@Data
@Component
@PropertySource("classpath:ftp.properties")
@ConfigurationProperties(prefix = "ftp")
public class FTPProperties {

  private String username;

  private String password;

  private String host;

  private Integer port;

  private String baseUrl;

  private Integer passiveMode = FTP.BINARY_FILE_TYPE;

  private String encoding="UTF-8";

  private int clientTimeout=120000;

  private int bufferSize;

  private int transferFileType=FTP.BINARY_FILE_TYPE;

  private boolean renameUploaded;

  private int retryTime;
}
```

创建FTPClientPool线程池

```java
/**
 * 自定义实现ftp连接池
 * @Auther: hrabbit
 * @Date: 2018-12-03 3:40 PM
 * @Description:
 */
@Slf4j
@SuppressWarnings("all")
public class FTPClientPool implements ObjectPool<FTPClient> {

  private static final int DEFAULT_POOL_SIZE = 10;

  public BlockingQueue<FTPClient> blockingQueue;

  private FTPClientFactory factory;

  public FTPClientPool(FTPClientFactory factory) throws Exception {
    this(DEFAULT_POOL_SIZE, factory);
  }

  public FTPClientPool(int poolSize, FTPClientFactory factory) throws Exception {
    this.factory = factory;
    this.blockingQueue = new ArrayBlockingQueue<FTPClient>(poolSize);
    initPool(poolSize);
  }

  /**
   * 初始化连接池
   * @param maxPoolSize
   *         最大连接数
   * @throws Exception
   */
  private void initPool(int maxPoolSize) throws Exception {
    int count = 0;
    while(count < maxPoolSize) {
      this.addObject();
      count++;
    }
  }

  /**
   * 从连接池中获取对象
   */
  @Override
  public FTPClient borrowObject() throws Exception {
    FTPClient client = blockingQueue.take();
    if(client == null) {
      client = factory.makeObject();
    } else if(!factory.validateObject(client)) {
      invalidateObject(client);
      client = factory.makeObject();
    }
    return client;
  }

  /**
   * 返还一个对象(链接)
   */
  @Override
  public void returnObject(FTPClient client) throws Exception {
    if ((client != null) && !blockingQueue.offer(client,2,TimeUnit.MINUTES)) {
      try {
        factory.destroyObject(client);
      } catch (Exception e) {
        throw e;
      }
    }
  }

  /**
   * 移除无效的对象(FTP客户端)
   */
  @Override
  public void invalidateObject(FTPClient client) throws Exception {
    blockingQueue.remove(client);
  }

  /**
   * 增加一个新的链接，超时失效
   */
  @Override
  public void addObject() throws Exception {
    blockingQueue.offer(factory.makeObject(), 2, TimeUnit.MINUTES);
  }

  /**
   * 重新连接
   */
  public FTPClient reconnect() throws Exception {
    return factory.makeObject();
  }

  /**
   * 获取空闲链接数(这里暂不实现)
   */
  @Override
  public int getNumIdle() {
    return blockingQueue.size();
  }

  /**
   * 获取正在被使用的链接数
   */
  @Override
  public int getNumActive() {
    return DEFAULT_POOL_SIZE - getNumIdle();
  }

  @Override
  public void clear() throws Exception {

  }

  /**
   * 关闭连接池
   */
  @Override
  public void close() {
    try {
      while(blockingQueue.iterator().hasNext()) {
        FTPClient client = blockingQueue.take();
        factory.destroyObject(client);
      }
    } catch(Exception e) {
      log.error("close ftp client pool failed...{}", e);
    }
  }

  /**
   * 增加一个新的链接，超时失效
   */
  public void addObject(FTPClient ftpClient) throws Exception {
    blockingQueue.put(ftpClient);
  }
}
```

**创建一个FTPClientFactory工厂类**

创建FTPClientFactory实现PoolableObjectFactory的接口，FTPClient工厂类，通过FTPClient工厂提供FTPClient实例的创建和销毁

```java
/**
 * FTPClient 工厂
 * @Auther: hrabbit
 * @Date: 2018-12-03 3:41 PM
 * @Description:
 */
@Slf4j
@SuppressWarnings("all")
public class FTPClientFactory implements PoolableObjectFactory<FTPClient> {

  private FTPProperties ftpProperties;

  public FTPClientFactory(FTPProperties ftpProperties) {
    this.ftpProperties = ftpProperties;
  }

  @Override
  public FTPClient makeObject() throws Exception {
    FTPClient ftpClient = new FTPClient();
    ftpClient.setControlEncoding(ftpProperties.getEncoding());
    ftpClient.setConnectTimeout(ftpProperties.getClientTimeout());
    try {
      ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());
      int reply = ftpClient.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        ftpClient.disconnect();
        log.warn("FTPServer refused connection");
        return null;
      }
      boolean result = ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
      ftpClient.setFileType(ftpProperties.getTransferFileType());
      if (!result) {
        log.warn("ftpClient login failed... username is {}", ftpProperties.getUsername());
      }
    } catch (Exception e) {
      log.error("create ftp connection failed...{}", e);
      throw e;
    }

    return ftpClient;
  }

  @Override
  public void destroyObject(FTPClient ftpClient) throws Exception {
    try {
      if(ftpClient != null && ftpClient.isConnected()) {
        ftpClient.logout();
      }
    } catch (Exception e) {
      log.error("ftp client logout failed...{}", e);
      throw e;
    } finally {
      if(ftpClient != null) {
        ftpClient.disconnect();
      }
    }

  }

  @Override
  public boolean validateObject(FTPClient ftpClient) {
    try {
      return ftpClient.sendNoOp();
    } catch (Exception e) {
      log.error("Failed to validate client: {}");
    }
    return false;
  }

  @Override
  public void activateObject(FTPClient obj) throws Exception {
    //Do nothing

  }

  @Override
  public void passivateObject(FTPClient obj) throws Exception {
    //Do nothing

  }
}
```

**创建FTPUtils.java的工具类**

FTPUtils.java中封装了上传、下载等方法，在项目启动的时候，在@PostConstruct注解的作用下通过执行init()的方法，创建FTPClientFactory工厂中，并初始化了FTPClientPool线程池，这样每次调用方法的时候，都直接从FTPClientPool中取出一个FTPClient对象

```java
/**
 * @Auther: hrabbit
 * @Date: 2018-12-03 3:47 PM
 * @Description:
 */
@Slf4j
@Component
public class FTPUtils {

  /**
   * FTP的连接池
   */
  @Autowired
  public static FTPClientPool ftpClientPool;
  /**
   * FTPClient对象
   */
  public static FTPClient ftpClient;


  private static FTPUtils ftpUtils;

  @Autowired
  private FTPProperties ftpProperties;

  /**
   * 初始化设置
   * @return
   */
  @PostConstruct
  public boolean init() {
    FTPClientFactory factory = new FTPClientFactory(ftpProperties);
    ftpUtils = this;
    try {
      ftpClientPool = new FTPClientPool(factory);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }


  /**
   * 获取连接对象
   * @return
   * @throws Exception
   */
  public static FTPClient getFTPClient() throws Exception {
    //初始化的时候从队列中取出一个连接
    if (ftpClient==null) {
      synchronized (ftpClientPool) {
        ftpClient = ftpClientPool.borrowObject();
      }
    }
    return ftpClient;
  }


  /**
   * 当前命令执行完成命令完成
   * @throws IOException
   */
  public void complete() throws IOException {
    ftpClient.completePendingCommand();
  }

  /**
   * 当前线程任务处理完成，加入到队列的最后
   * @return
   */
  public void disconnect() throws Exception {
    ftpClientPool.addObject(ftpClient);
  }

  /**
   * Description: 向FTP服务器上传文件
   *
   * @Version1.0
   * @param remoteFile
   *      上传到FTP服务器上的文件名
   * @param input
   *      本地文件流
   * @return 成功返回true，否则返回false
   */
  public static boolean uploadFile(String remoteFile, InputStream input) {
    boolean result = false;
    try {
      getFTPClient();
      ftpClient.enterLocalPassiveMode();
      result = ftpClient.storeFile(remoteFile, input);
      input.close();
      ftpClient.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Description: 向FTP服务器上传文件
   *
   * @Version1.0
   * @param remoteFile
   *      上传到FTP服务器上的文件名
   * @param localFile
   *      本地文件
   * @return 成功返回true，否则返回false
   */
  public static boolean uploadFile(String remoteFile, String localFile){
    FileInputStream input = null;
    try {
      input = new FileInputStream(new File(localFile));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return uploadFile(remoteFile, input);
  }

  /**
   * 拷贝文件
   * @param fromFile
   * @param toFile
   * @return
   * @throws Exception
   */
  public boolean copyFile(String fromFile, String toFile) throws Exception {
    InputStream in=getFileInputStream(fromFile);
    getFTPClient();
    boolean flag = ftpClient.storeFile(toFile, in);
    in.close();
    return flag;
  }

  /**
   * 获取文件输入流
   * @param fileName
   * @return
   * @throws IOException
   */
  public static InputStream getFileInputStream(String fileName) throws Exception {
    ByteArrayOutputStream fos=new ByteArrayOutputStream();
    getFTPClient();
    ftpClient.retrieveFile(fileName, fos);
    ByteArrayInputStream in=new ByteArrayInputStream(fos.toByteArray());
    fos.close();
    return in;
  }

  /**
   * Description: 从FTP服务器下载文件
   *
   * @Version1.0
   * @return
   */
  public static boolean downFile(String remoteFile, String localFile){
    boolean result = false;
    try {
      getFTPClient();
      OutputStream os = new FileOutputStream(localFile);
      ftpClient.retrieveFile(remoteFile, os);
      ftpClient.logout();
      ftpClient.disconnect();
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  /**
   * 从ftp中获取文件流
   * @param filePath
   * @return
   * @throws Exception
   */
  public static InputStream getInputStream(String filePath) throws Exception {
    getFTPClient();
    InputStream inputStream = ftpClient.retrieveFileStream(filePath);
    return inputStream;
  }

  /**
   * ftp中文件重命名
   * @param fromFile
   * @param toFile
   * @return
   * @throws Exception
   */
  public boolean rename(String fromFile,String toFile) throws Exception {
    getFTPClient();
    boolean result = ftpClient.rename(fromFile,toFile);
    return result;
  }

  /**
   * 获取ftp目录下的所有文件
   * @param dir
   * @return
   */
  public FTPFile[] getFiles(String dir) throws Exception {
    getFTPClient();
    FTPFile[] files = new FTPFile[0];
    try {
      files = ftpClient.listFiles(dir);
    }catch (Throwable thr){
      thr.printStackTrace();
    }
    return files;
  }

  /**
   * 获取ftp目录下的某种类型的文件
   * @param dir
   * @param filter
   * @return
   */
  public FTPFile[] getFiles(String dir, FTPFileFilter filter) throws Exception {
    getFTPClient();
    FTPFile[] files = new FTPFile[0];
    try {
      files = ftpClient.listFiles(dir, filter);
    }catch (Throwable thr){
      thr.printStackTrace();
    }
    return files;
  }

  /**
   * 创建文件夹
   * @param remoteDir
   * @return 如果已经有这个文件夹返回false
   */
  public boolean makeDirectory(String remoteDir) throws Exception {
    getFTPClient();
    boolean result = false;
    try {
      result = ftpClient.makeDirectory(remoteDir);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public boolean mkdirs(String dir) throws Exception {
    boolean result = false;
    if (null == dir) {
      return result;
    }
    getFTPClient();
    ftpClient.changeWorkingDirectory("/");
    StringTokenizer dirs = new StringTokenizer(dir, "/");
    String temp = null;
    while (dirs.hasMoreElements()) {
      temp = dirs.nextElement().toString();
      //创建目录
      ftpClient.makeDirectory(temp);
      //进入目录
      ftpClient.changeWorkingDirectory(temp);
      result = true;
    }
    ftpClient.changeWorkingDirectory("/");
    return result;
  }
}
```

**创建FtpClientTest.java测试类**

上传一张图片到FTP服务器，并将文件重新命名为hrabbit.jpg，代码如下:

```java
/**
 * FtpClient测试
 * @Auther: hrabbit
 * @Date: 2018-12-21 9:14 PM
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FtpClientTest {

  /**
   * 测试上传
   */
  @Test
  public void uploadFile(){
    boolean flag = FTPUtils.uploadFile("hrabbit.jpg", "/Users/mrotaku/Downloads/klklklkl_4x.jpg");
    Assert.assertEquals(true, flag);
  }
}
```

程序完美运行，这时候我们查看我们的FTP服务器，http://localhost:8866/hrabbit.jpg

![img](MarkDownImages/ftp.assets/20181223170857480-16576462549916.jpg)