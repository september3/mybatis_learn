package org.apache.ibatis.parsing;

/**
 * 标记处理器
 * @author Clinton Begin
 */
public interface TokenHandler {

  String handleToken(String content);
}

