package org.apache.ibatis.datasource.pooled;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * 池型数据源
 * 使用池型数据源，在池中保存有多个数据库连接，可以供多个数据库访问线程同时获取现成的不同的数据库连接，
 * 既保证了数据访问的安全性，也能极大的提升系统的运行速度。
 * @author Administrator
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

  /**
   * 用于获取池型数据源的实例，同时，由于PooledDataSourceFactory继承了UnpooledDataSourceFactory，
   * 也就有了UnpooledDataSourceFactory的属性和功能
   *
   */
  public PooledDataSourceFactory() {
    this.dataSource = new PooledDataSource();
  }

}
