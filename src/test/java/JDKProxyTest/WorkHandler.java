package JDKProxyTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author sunlele
 * @className WorkHandler
 * @date 2019/5/27 09:32
 **/
public class WorkHandler implements InvocationHandler {

    /**
     * 代理类中真实对象
     */
    private Object obj;

    /**
     * 为真实对象进行赋值
     * @param obj
     */
    public WorkHandler(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在真实的对象执行之前我们可以添加自己的操作
        System.out.println("before invoke。。。");
        Object invoke = method.invoke(obj, args);
        //在真实的对象执行之后我们可以添加自己的操作
        System.out.println("after invoke。。。");
        return invoke;
    }
}
