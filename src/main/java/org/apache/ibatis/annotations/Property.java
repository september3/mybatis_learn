package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入属性值的注释
 *
 * @since 3.4.2
 * @author Kazuki Shimizu
 * @see CacheNamespace
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Property {

  /**
   * 目标属性名称
   */
  String name();

  /**
   * 属性值或占位符.
   */
  String value();
}