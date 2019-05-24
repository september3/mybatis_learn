package org.apache.ibatis.datasource.unpooled;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;

/**
 * 非池型数据源
 * 非池型是相对池型而言的，池型数据源统筹管理着一个数据库连接池，在这个池中拥有指定数量的数据库连接实例connection可供使用，
 * 其内部采用一定的规则指定数据连接对象的使用、创建、销毁规则，来为多线程数据库访问提供及时有效的数据库连接。
 *
 * 非池型数据源，即保持有一个数据库连接实例的数据源
 *
 * @author Administrator
 */
public class UnpooledDataSource implements DataSource {
  /**
   * 驱动类加载器：目的是 从磁盘中加载数据库驱动到内存
   */
  private ClassLoader driverClassLoader;
  /**
   * 驱动器属性：用以保存手动设置的数据库驱动属性，如果不进行设置，则使用默认值。
   * 当前属性设置的键一般使用“driver.”为前缀
   */
  private Properties driverProperties;
  /**
   * 数据库驱动注册器，其内部保存着所有已注册的数据库驱动类实例，
   * 这个字段是static修饰的，表示在数据源类加载的时候就会创建，这个时候创建的其实是个空集合。
   * 再者使用ConcurrentHashMap集合，这是一个线程安全的键值对型集合，它几乎可以与HashTable通用（二者都是线程安全的集合类）。
   */
  private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();

  /**
   * 数据库驱动类名
   */
  private String driver;
  /**
   * 数据库服务器URL地址
   */
  private String url;
  /**
   * 数据库服务器连接用户名
   */
  private String username;
  /**
   * 数据库服务器连接密码
   */
  private String password;

  /**
   * 是否自动提交
   */
  private Boolean autoCommit;
  /**
   * 默认的事务隔离级别
   */
  private Integer defaultTransactionIsolationLevel;

  /**
   * 验证的目的是确保使用本类加载器获取的驱动器类与在registeredDrivers中保存的对应驱动类实例的类是同一类型
   */
  static {
    Enumeration<Driver> drivers = DriverManager.getDrivers();
    while (drivers.hasMoreElements()) {
      Driver driver = drivers.nextElement();
      registeredDrivers.put(driver.getClass().getName(), driver);
    }
  }

  /**
   * 无参构造器
   * 这个构造器在数据源工厂的无参构造器中被调用，用于创建一个空的UnpooledDataSource实例，
   * 然后使用工厂类中的setProperties()方法，为这个空实例中的各个字段进行赋值（
   * 采用上面提到的第二种方式进行读取配置信息并保存到实例中），从而创建一个饱满的实例，
   */
  public UnpooledDataSource() {
  }

  public UnpooledDataSource(String driver, String url, String username, String password) {
    this.driver = driver;
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public UnpooledDataSource(String driver, String url, Properties driverProperties) {
    this.driver = driver;
    this.url = url;
    this.driverProperties = driverProperties;
  }

  public UnpooledDataSource(ClassLoader driverClassLoader, String driver, String url, String username, String password) {
    this.driverClassLoader = driverClassLoader;
    this.driver = driver;
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public UnpooledDataSource(ClassLoader driverClassLoader, String driver, String url, Properties driverProperties) {
    this.driverClassLoader = driverClassLoader;
    this.driver = driver;
    this.url = url;
    this.driverProperties = driverProperties;
  }

  /**
   * 获取数据源连接的方法
   * 无参方法，内部使用默认的用户名与面目进行数据库连接
   * @return
   * @throws SQLException
   */
  @Override
  public Connection getConnection() throws SQLException {
    //真正的执行数据库连接并获取这个连接的方法
    return doGetConnection(username, password);
  }

  /**
   * 指定用户名与密码的获取数据源连接的方法
   * 使用指定的用户名与密码进行数据库连接，并将该连接返回
   * @param username
   * @param password
   * @return
   * @throws SQLException
   */
  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return doGetConnection(username, password);
  }

  /**
   * 表示设置数据源连接数据库的最长等待时间，以秒为单位
   * @param loginTimeout
   */
  @Override
  public void setLoginTimeout(int loginTimeout) {
    DriverManager.setLoginTimeout(loginTimeout);
  }

  /**
   * 表示获取数据源连接到数据库的最长等待时间
   * @return
   */
  @Override
  public int getLoginTimeout() {
    return DriverManager.getLoginTimeout();
  }

  /**
   * 设置数据源的日志输出者（log writer）为给定的一个PrintWriter实例
   * getLogWriter()：获取数据源的日志输出者。
   * @param logWriter
   */
  @Override
  public void setLogWriter(PrintWriter logWriter) {
    DriverManager.setLogWriter(logWriter);
  }

  /**
   * 获取这个DataSource所使用的所有Logger的父Logger。
   * @return
   */
  @Override
  public PrintWriter getLogWriter() {
    return DriverManager.getLogWriter();
  }

  public ClassLoader getDriverClassLoader() {
    return driverClassLoader;
  }

  public void setDriverClassLoader(ClassLoader driverClassLoader) {
    this.driverClassLoader = driverClassLoader;
  }

  public Properties getDriverProperties() {
    return driverProperties;
  }

  public void setDriverProperties(Properties driverProperties) {
    this.driverProperties = driverProperties;
  }

  public String getDriver() {
    return driver;
  }

  public synchronized void setDriver(String driver) {
    this.driver = driver;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean isAutoCommit() {
    return autoCommit;
  }

  public void setAutoCommit(Boolean autoCommit) {
    this.autoCommit = autoCommit;
  }

  public Integer getDefaultTransactionIsolationLevel() {
    return defaultTransactionIsolationLevel;
  }

  public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
    this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
  }

  /**
   * 用以连接数据库并且获取数据库连接的方法
   * @param username 用户名
   * @param password 密码
   * @return 数据库连接
   * @throws SQLException
   */
  private Connection doGetConnection(String username, String password) throws SQLException {
    //Properties属性变量用于存储传递的参数
    Properties props = new Properties();
    if (driverProperties != null) {
      props.putAll(driverProperties);
    }
    if (username != null) {
      props.setProperty("user", username);
    }
    if (password != null) {
      props.setProperty("password", password);
    }
    return doGetConnection(props);
  }

  private Connection doGetConnection(Properties properties) throws SQLException {
    //初始化驱动器
    initializeDriver();
    Connection connection = DriverManager.getConnection(url, properties);
    //数据库连接的配置
    configureConnection(connection);
    return connection;
  }

  /**
   * 驱动器初始化---同步方法
   * 加载驱动器类，并将其注册到DriverManager中，同时将其保存到本实例的registeredDrivers中
   * @throws SQLException
   */
  private synchronized void initializeDriver() throws SQLException {
    //判断驱动器
    if (!registeredDrivers.containsKey(driver)) {
      Class<?> driverType;
      try {
        if (driverClassLoader != null) {
          driverType = Class.forName(driver, true, driverClassLoader);
        } else {
          driverType = Resources.classForName(driver);
        }
        // DriverManager requires the driver to be loaded via the system ClassLoader.
        // http://www.kfu.com/~nsayer/Java/dyn-jdbc.html
        Driver driverInstance = (Driver)driverType.newInstance();
        DriverManager.registerDriver(new DriverProxy(driverInstance));
        registeredDrivers.put(driver, driverInstance);
      } catch (Exception e) {
        throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
      }
    }
  }

  /**
   * 数据库连接配置
   * @param conn
   * @throws SQLException
   */
  private void configureConnection(Connection conn) throws SQLException {
    if (autoCommit != null && autoCommit != conn.getAutoCommit()) {
      conn.setAutoCommit(autoCommit);
    }
    if (defaultTransactionIsolationLevel != null) {
      conn.setTransactionIsolation(defaultTransactionIsolationLevel);
    }
  }

  /**
   * 真正的驱动器动态代理类
   */
  private static class DriverProxy implements Driver {
    private Driver driver;

    DriverProxy(Driver d) {
      this.driver = d;
    }

    @Override
    public boolean acceptsURL(String u) throws SQLException {
      return this.driver.acceptsURL(u);
    }

    @Override
    public Connection connect(String u, Properties p) throws SQLException {
      return this.driver.connect(u, p);
    }

    @Override
    public int getMajorVersion() {
      return this.driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
      return this.driver.getMinorVersion();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
      return this.driver.getPropertyInfo(u, p);
    }

    @Override
    public boolean jdbcCompliant() {
      return this.driver.jdbcCompliant();
    }

    // @Override only valid jdk7+
    public Logger getParentLogger() {
      return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLException(getClass().getName() + " is not a wrapper.");
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  // @Override only valid jdk7+
  public Logger getParentLogger() {
    // requires JDK version 1.6
    return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  }

}
