
package org.apache.ibatis.mapping;

import javax.sql.DataSource;

import org.apache.ibatis.transaction.TransactionFactory;

/**
 *
 * 该类相当于Configuration.XML文件中<environment></environment>标签，里面包含id、transactionFactory和dataSource
 *
 * 构建者模式：构建者模式一般用于构建复杂对象时，将复杂对象分割成许多小对象进行分别构建，然后整合在一起形成一个大对象，
 * 这样做能很好的规范对象构建的细节过程
 *
 * 内部使用Builder建造者模式，只有当Environment对象中的内部构建方法builder（）被显示调用之后才会在内存中进行创建
 * 实现了懒加载
 */
public final class Environment {
  /**
   * 环境ID
   */
  private final String id;
  /**
   * 事务工厂
   */
  private final TransactionFactory transactionFactory;
  /**
   * 数据源
   */
  private final DataSource dataSource;

  /**
   * 环境的带参数判断的构造函数
   * @param id id
   * @param transactionFactory transaction
   * @param dataSource dataSource
   */
  public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
    if (id == null) {
      throw new IllegalArgumentException("Parameter 'id' must not be null");
    }
    if (transactionFactory == null) {
      throw new IllegalArgumentException("Parameter 'transactionFactory' must not be null");
    }
    this.id = id;
    if (dataSource == null) {
      throw new IllegalArgumentException("Parameter 'dataSource' must not be null");
    }
    this.transactionFactory = transactionFactory;
    this.dataSource = dataSource;
  }

  /**
   * 内部静态Builder---建造者模式
   */
  public static class Builder {
    private String id;
    private TransactionFactory transactionFactory;
    private DataSource dataSource;

    /**
     * 构建Id
     * @param id id
     */
    public Builder(String id) {
      this.id = id;
    }

    /**
     * 构建事务工厂
     * @param transactionFactory transactionFactory
     * @return transactionFactory
     */
    public Builder transactionFactory(TransactionFactory transactionFactory) {
      this.transactionFactory = transactionFactory;
      return this;
    }

    /**
     * 构建数据源
     * @param dataSource dataSource
     * @return dataSource
     */
    public Builder dataSource(DataSource dataSource) {
      this.dataSource = dataSource;
      return this;
    }

    public String id() {
      return this.id;
    }

    public Environment build() {
      return new Environment(this.id, this.transactionFactory, this.dataSource);
    }

  }

  public String getId() {
    return this.id;
  }

  public TransactionFactory getTransactionFactory() {
    return this.transactionFactory;
  }

  public DataSource getDataSource() {
    return this.dataSource;
  }

}
