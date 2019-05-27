package org.apache.ibatis.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;

/**
 * 类型别名注册器
 *    类型别名：MyBatis中的类型别名就是针对MyBatis中常用的类型进行别名设置，使用别名来代替具体的类型，
 * 简单点说就是，将具体的类型以别名为键，保存到一个HashMap之中，方便存取。
 *    类型别名的用途：MyBatis中的类型别名主要用于取代复杂的类型全限定名，
 * 用于映射器配置文件中进行参数类型与返回结果类型的设置，MyBatis会在进行数据库操作之前进行参数类型别名的解析操作
 * 获取具体的参数类型，又会在数据库操作之后进行结果类型别名的解析获取具体的结果类型，再
 * 通过之后要研究的类型处理器进行类型处理来将参数和结果分别进行匹配映射。
 * @author Clinton Begin
 */
public class TypeAliasRegistry {

  /**
   * 保存类型注册别名列表
   */
  private final Map<String, Class<?>> typeAliases = new HashMap<>();

  /**
   * 构造函数里注册系统内置的类型别名
   */
  public TypeAliasRegistry() {
    registerAlias("string", String.class);
    //基本数据类型
    registerAlias("byte", Byte.class);
    registerAlias("long", Long.class);
    registerAlias("short", Short.class);
    registerAlias("int", Integer.class);
    registerAlias("integer", Integer.class);
    registerAlias("double", Double.class);
    registerAlias("float", Float.class);
    registerAlias("boolean", Boolean.class);

    //基本数据类型数组类型
    registerAlias("byte[]", Byte[].class);
    registerAlias("long[]", Long[].class);
    registerAlias("short[]", Short[].class);
    registerAlias("int[]", Integer[].class);
    registerAlias("integer[]", Integer[].class);
    registerAlias("double[]", Double[].class);
    registerAlias("float[]", Float[].class);
    registerAlias("boolean[]", Boolean[].class);

    registerAlias("_byte", byte.class);
    registerAlias("_long", long.class);
    registerAlias("_short", short.class);
    registerAlias("_int", int.class);
    registerAlias("_integer", int.class);
    registerAlias("_double", double.class);
    registerAlias("_float", float.class);
    registerAlias("_boolean", boolean.class);

    registerAlias("_byte[]", byte[].class);
    registerAlias("_long[]", long[].class);
    registerAlias("_short[]", short[].class);
    registerAlias("_int[]", int[].class);
    registerAlias("_integer[]", int[].class);
    registerAlias("_double[]", double[].class);
    registerAlias("_float[]", float[].class);
    registerAlias("_boolean[]", boolean[].class);
    //日期数据类型
    registerAlias("date", Date.class);
    registerAlias("decimal", BigDecimal.class);
    registerAlias("bigdecimal", BigDecimal.class);
    registerAlias("biginteger", BigInteger.class);
    registerAlias("object", Object.class);
    //日期数组数据类型
    registerAlias("date[]", Date[].class);
    registerAlias("decimal[]", BigDecimal[].class);
    registerAlias("bigdecimal[]", BigDecimal[].class);
    registerAlias("biginteger[]", BigInteger[].class);
    registerAlias("object[]", Object[].class);
    //集合型
    registerAlias("map", Map.class);
    registerAlias("hashmap", HashMap.class);
    registerAlias("list", List.class);
    registerAlias("arraylist", ArrayList.class);
    registerAlias("collection", Collection.class);
    registerAlias("iterator", Iterator.class);

    registerAlias("ResultSet", ResultSet.class);
  }

  /**
   * 如果无法分配类型，则抛出类强制转换异常
   * @param string
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> Class<T> resolveAlias(String string) {
    try {
      if (string == null) {
        return null;
      }
      // issue #748
      String key = string.toLowerCase(Locale.ENGLISH);
      Class<T> value;
      if (typeAliases.containsKey(key)) {
        value = (Class<T>) typeAliases.get(key);
      } else {
        value = (Class<T>) Resources.classForName(string);
      }
      return value;
    } catch (ClassNotFoundException e) {
      throw new TypeException("Could not resolve type alias '" + string + "'.  Cause: " + e, e);
    }
  }

  /**
   * 包 统一注册方式
   * @param packageName
   */
  public void registerAliases(String packageName) {
    //表明注册指定包名下的所有类
    registerAliases(packageName, Object.class);
  }

  /**
   * 扫描包下所有继承自supertype的类型别名
   * @param packageName
   * @param superType  限定要注册的类的来源，只有继承自该类 才能够被注册
   */
  public void registerAliases(String packageName, Class<?> superType) {
    ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
    resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
    Set<Class<? extends Class<?>>> typeSet = resolverUtil.getClasses();
    for (Class<?> type : typeSet) {
      // Ignore inner classes and interfaces (including package-info.java)
      // Skip also inner classes. See issue #6
      if (!type.isAnonymousClass() && !type.isInterface() && !type.isMemberClass()) {
        registerAlias(type);
      }
    }
  }

  /**
   * 设置别名
   * @param type
   */
  public void registerAlias(Class<?> type) {
    //针对未显式指定别名名称的类型，通过Class.getSimpleName()方法来获取类型的别名名称，
    // 其获取到的其实是类型的首字母小写形式的名称
    String alias = type.getSimpleName();
    //针对使使用@Alias注解显式指定别名名称的类型（value的值），直接获取该注解中value的值作为别名名称即可
    Alias aliasAnnotation = type.getAnnotation(Alias.class);
    if (aliasAnnotation != null) {
      alias = aliasAnnotation.value();
    }
    //调用核心注册方法来实现类型别名的注册
    registerAlias(alias, type);
  }

  /**
   * 核心类型别名注册方法
   * @param alias 别名
   * @param value
   */
  public void registerAlias(String alias, Class<?> value) {
    if (alias == null) {
      throw new TypeException("The parameter alias cannot be null");
    }
    // issue #748
    String key = alias.toLowerCase(Locale.ENGLISH);
    if (typeAliases.containsKey(key) && typeAliases.get(key) != null && !typeAliases.get(key).equals(value)) {
      throw new TypeException("The alias '" + alias + "' is already mapped to the value '" + typeAliases.get(key).getName() + "'.");
    }
    typeAliases.put(key, value);
  }

  /**
   * 逐个注册方式
   * @param alias
   * @param value
   */
  public void registerAlias(String alias, String value) {
    try {
      registerAlias(alias, Resources.classForName(value));
    } catch (ClassNotFoundException e) {
      throw new TypeException("Error registering type alias " + alias + " for " + value + ". Cause: " + e, e);
    }
  }

  /**
   * @since 3.2.2
   */
  public Map<String, Class<?>> getTypeAliases() {
    return Collections.unmodifiableMap(typeAliases);
  }

}
