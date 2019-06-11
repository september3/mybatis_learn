package org.apache.ibatis.executor;
/**
 * 单例模式的体现
 * 当前类的功能：将执行上下文信息的收集独立出来并集中到一处。
 * @author Clinton Begin
 */
public class ErrorContext {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator","\n");
  /**
   * 定义一个ThreadLocal来存放ErrorContext，使得该变量属于每个线程各自的数据
   */
  private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<>();

  private ErrorContext stored;
  private String resource;
  private String activity;
  private String object;
  private String message;
  private String sql;
  private Throwable cause;

  /**
   * 私有构造函数，避免被外部实例初始化
   */
  private ErrorContext() {
  }

  /**
   *创建实例的静态方法
   * @return
   */
  public static ErrorContext instance() {
    //获取实例的方法
    ErrorContext context = LOCAL.get();
    if (context == null) {
      context = new ErrorContext();
      //将创建的ErrorContext放入ThreadLocal中
      LOCAL.set(context);
    }
    return context;
  }

  public ErrorContext store() {
    ErrorContext newContext = new ErrorContext();
    newContext.stored = this;
    LOCAL.set(newContext);
    return LOCAL.get();
  }

  public ErrorContext recall() {
    if (stored != null) {
      LOCAL.set(stored);
      stored = null;
    }
    return LOCAL.get();
  }

  public ErrorContext resource(String resource) {
    this.resource = resource;
    return this;
  }

  public ErrorContext activity(String activity) {
    this.activity = activity;
    return this;
  }

  public ErrorContext object(String object) {
    this.object = object;
    return this;
  }

  public ErrorContext message(String message) {
    this.message = message;
    return this;
  }

  public ErrorContext sql(String sql) {
    this.sql = sql;
    return this;
  }

  public ErrorContext cause(Throwable cause) {
    this.cause = cause;
    return this;
  }

  /**
   * 使用完之后，对ThreadLocal中内容进行清空
   * @return
   */
  public ErrorContext reset() {
    resource = null;
    activity = null;
    object = null;
    message = null;
    sql = null;
    cause = null;
    LOCAL.remove();
    return this;
  }

  @Override
  public String toString() {
    StringBuilder description = new StringBuilder();

    // 自定义的信息
    if (this.message != null) {
      description.append(LINE_SEPARATOR);
      description.append("### ");
      description.append(this.message);
    }

    // 错误来源
    if (resource != null) {
      description.append(LINE_SEPARATOR);
      description.append("### The error may exist in ");
      description.append(resource);
    }

    // object
    if (object != null) {
      description.append(LINE_SEPARATOR);
      description.append("### The error may involve ");
      description.append(object);
    }

    // activity
    if (activity != null) {
      description.append(LINE_SEPARATOR);
      description.append("### The error occurred while ");
      description.append(activity);
    }

    // activity
    if (sql != null) {
      description.append(LINE_SEPARATOR);
      description.append("### SQL: ");
      description.append(sql.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim());
    }

    // cause
    if (cause != null) {
      description.append(LINE_SEPARATOR);
      description.append("### Cause: ");
      description.append(cause.toString());
    }

    return description.toString();
  }

}
