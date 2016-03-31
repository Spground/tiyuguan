package cn.edu.dlut.tiyuguan;

import java.lang.reflect.Field;

/**
 * Created by asus on 2016/3/31.
 */
public class FiledTest {
    public static void main(String[] args) {
        A a = new A();
        Field field = null;
        try {
          field = a.getClass().getDeclaredField("a");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            field.setAccessible(true);
            field.set(a, "2012");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.print(a.a);
    }


}

class A {
    public int a;
}