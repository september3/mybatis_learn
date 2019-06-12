package org.apache.ibatis.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

/**
 *
 * 创建SqlSession实例---建造者模式
 * Builds {@link SqlSession} instances.
 *
 * @author Clinton Begin
 */
public class SqlSessionFactoryBuilder {

  /**
   * Reader读取mybatis配置文件，传入构造方法
   * @param reader
   * @return
   */
  public SqlSessionFactory build(Reader reader) {
    return build(reader, null, null);
  }


  public SqlSessionFactory build(Reader reader, String environment) {
    return build(reader, environment, null);
  }

  /**
   * Mybatis配置文件+Properties
   * @param reader
   * @param properties
   * @return
   */
  public SqlSessionFactory build(Reader reader, Properties properties) {
    return build(reader, null, properties);
  }

  /**
   * 最终被调用的Build方法
   * @param reader
   * @param environment
   * @param properties
   * @return
   */
  public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
    try {
      //将解析的相关配置信息都封装为Configuration
      XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      //ErrorContext使用ThreadLocal进行修饰，在使用完之后必须清空
      ErrorContext.instance().reset();
      try {
        reader.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  public SqlSessionFactory build(InputStream inputStream) {
    return build(inputStream, null, null);
  }

  public SqlSessionFactory build(InputStream inputStream, String environment) {
    return build(inputStream, environment, null);
  }

  public SqlSessionFactory build(InputStream inputStream, Properties properties) {
    return build(inputStream, null, properties);
  }

  public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    try {
      //通过XMLConfigBuilder解析Mybatis配置文件，然后创建默认的SQLSessionFactory
      XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        inputStream.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  public SqlSessionFactory build(Configuration config) {
    //创建默认的SQLSessionFactory
    return new DefaultSqlSessionFactory(config);
  }

}
