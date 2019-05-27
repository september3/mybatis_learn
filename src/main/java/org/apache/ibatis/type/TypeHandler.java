package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器
 * @author Clinton Begin
 */
public interface TypeHandler<T> {

  /**
   * 设置参数
   * @param ps
   * @param i
   * @param parameter
   * @param jdbcType
   * @throws SQLException
   */
  void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

  /**
   * 获取结果---从ResultSet中进行获取
   * 通过列名称columnName进行获取
   * @param rs
   * @param columnName
   * @return
   * @throws SQLException
   */
  T getResult(ResultSet rs, String columnName) throws SQLException;

  /**
   * 获取结果
   * 通过列下标columnIndex进行获取
   * @param rs
   * @param columnIndex
   * @return
   * @throws SQLException
   */
  T getResult(ResultSet rs, int columnIndex) throws SQLException;

  /**
   * 获取结果---从CallableStatement中进行获取
   * @param cs
   * @param columnIndex
   * @return
   * @throws SQLException
   */
  T getResult(CallableStatement cs, int columnIndex) throws SQLException;

}
