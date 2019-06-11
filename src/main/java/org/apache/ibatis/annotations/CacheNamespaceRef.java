package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation that reference a cache.
 * <p>
 * If you use this annotation, should be specified either {@link #value()} or {@link #name()} attribute.
 * </p>
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CacheNamespaceRef {

  /**
   * A namespace type to reference a cache (the namespace name become a FQCN of specified type).
   */
  Class<?> value() default void.class;

  /**
   * A namespace name to reference a cache.
   * @since 3.4.2
   */
  String name() default "";
}
