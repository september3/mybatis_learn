package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;

/**
 * 缓存命名空间
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CacheNamespace {
  Class<? extends org.apache.ibatis.cache.Cache> implementation() default PerpetualCache.class;

  /**
   *缓存策略默认使用  最近使用的
   * @return
   */
  Class<? extends org.apache.ibatis.cache.Cache> eviction() default LruCache.class;

  /**
   * 缓存刷新间隔
   * @return
   */
  long flushInterval() default 0;

  int size() default 1024;

  boolean readWrite() default true;

  boolean blocking() default false;

  /**
   * 实现对象属性值.
   * @since 3.4.2
   */
  Property[] properties() default {};

}
