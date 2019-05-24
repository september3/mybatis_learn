package org.apache.ibatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 包装数据库的连接
 * 处理连接生命周期，包括：创建，准备，提交/回滚和关闭
 * @author Administrator
 */
public interface Transaction {


  /**
   * 检索内部数据库连接
   * @return 数据库连接
   * @throws SQLException
   */
  Connection getConnection() throws SQLException;

  /**
   * 提交内部数据库连接
   * @throws SQLException
   */
  void commit() throws SQLException;

  /**
   * 回滚内部数据库连接
   * @throws SQLException
   */
  void rollback() throws SQLException;

  /**
   * 关闭内部数据库连接
   * @throws SQLException
   */
  void close() throws SQLException;

  /**
   * 设置时获取事务超时
   * @throws SQLException
   */
  Integer getTimeout() throws SQLException;

}
