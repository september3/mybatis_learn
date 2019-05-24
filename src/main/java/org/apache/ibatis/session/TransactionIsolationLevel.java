package org.apache.ibatis.session;

import java.sql.Connection;

/**
 * 事务隔离级别
 * @author Administrator
 */
public enum TransactionIsolationLevel {
  /**
   * 不支持事务隔离
   * TRANSACTION_NONE= 0;
   */
  NONE(Connection.TRANSACTION_NONE),
  /**
   * 读已提交
   * int TRANSACTION_READ_COMMITTED   = 2
   * 当前隔离级别避免了脏读，不可重复读和幻象读会发生
   */
  READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
  /**
   * 读未提交
   * TRANSACTION_READ_UNCOMMITTED = 1
   * 当前隔离级别会造成脏读、不可重复读、幻象读取
   */
  READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
  /**
   * 重复读
   * int TRANSACTION_REPEATABLE_READ  = 4
   * 当前隔离级别避免了脏读和不可重复读，幻象读会发生
   */
  REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
  /**
   * 序列化
   *当前事务隔离级别避免了脏读、不可重复度、幻象读
   */
  SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

  /**
   * 代表事务隔离级别
   */
  private final int level;

  TransactionIsolationLevel(int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
  }
}
