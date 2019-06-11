package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

/**
 * @author Clinton Begin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Arg {
  boolean id() default false;

  String column() default "";

  /**
   * java类默认类型为.class
   * @return
   */
  Class<?> javaType() default void.class;

    /**
     *JdbcType类型从枚举类中进行获取
     *  @return
     */
  JdbcType jdbcType() default JdbcType.UNDEFINED;

  Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;

  String select() default "";

  String resultMap() default "";

  String name() default "";

  /**
   * @since 3.5.0
   */
  String columnPrefix() default "";
}
