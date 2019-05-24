package org.apache.ibatis.datasource;

import java.util.Properties;
import javax.sql.DataSource;
/**
 * 数据源工厂---工厂模式
 * @author Administrator
 */
public interface DataSourceFactory {

  /**
   * 设置属性，被XMLConfigBuilder所调用
   * @param props 属性
   */
  void setProperties(Properties props);

  /**
   * 生产数据源，直接得到javax.sql.DataSource
   * @return DataSource
   */
  DataSource getDataSource();

}
