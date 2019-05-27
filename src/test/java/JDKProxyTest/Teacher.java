package JDKProxyTest;

/**
 * 真正的对象
 * @author sunlele
 * @className Teacher
 * @date 2019/5/27 09:31
 **/
public class Teacher implements People {
    @Override
    public String work() {
        System.out.println("老师教书育人");
        return "教书";
    }
}
