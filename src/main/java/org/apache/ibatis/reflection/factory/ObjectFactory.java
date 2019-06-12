package org.apache.ibatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 *生产对象
 * @author Clinton Begin
 */
public interface ObjectFactory {

  /**
   * 设置配置属性.
   * @param properties configuration properties
   */
  void setProperties(Properties properties);

  /**
   * 使用默认的构造方法创建对象
   * @param type Object type
   * @return
   */
  <T> T create(Class<T> type);

  /**
   * 使用指定的构造函数和参数来创建对象
   * @param type Object type
   * @param constructorArgTypes Constructor argument types
   * @param constructorArgs Constructor argument values
   * @return
   */
  <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

  /**
   * 判断对象是否为Collection
   * @param type Object type
   * @return whether it is a collection or not
   * @since 3.1.0
   */
  <T> boolean isCollection(Class<T> type);

}
