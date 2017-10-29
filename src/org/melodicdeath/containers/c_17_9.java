package org.melodicdeath.containers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zt.melody on 2017/10/29.
 */

class Groundhog {
    protected int number;

    public Groundhog(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Groundhost #" + number;
    }

    // 默认的Object.equals只是比较对象的地址
    // 当对象用来做Map的键时，必须重写equals和hashCode方法。否则使用散列的数据结构就无法正确处理键。
    // 使用散列的目的在于：用一个对象来查找另一个对象。当然用TreeMap和自定义Map（继承AbstractMap）也可以达到目的。
    // 如果键没有按照任何特定顺序保存，则只能使用简单的线性查询，而线性查询是最慢的查询方式。因此解决方案之一就是保持键
    // 的排序状态，然后使用Collections.binarySearch进行查询
    // 散列的价值在于速度：散列使得查询得以快速进行。它将键保存在某持，以便能够很快找到。
    /*
    * 正确的equals必须满足5个条件
    * 1.自反性。对任意x，x.equals(x)一定返回true
    * 2.对称性。对任意x和y，如果x.eqauls(y)返回true，则x.equals(y)也返回true
    * 3.传递性。对任意x、y、z，如果有x.equals(y)返回true,y.equals(z)返回true，则x.equals(z)一定返回true
    * 4.一致性。对任意x和y,如果对象中用于等价比较的信息没有改变，那么无论调用x.equals(y)多少次，返回的结果应该保持一致
    * 5.对任何不是null的x，x.equals(null)一定返回false
    * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Groundhog groundhog = (Groundhog) o;

        return number == groundhog.number;
    }

    /*
    * */
    @Override
    public int hashCode() {
        return number;
    }
}

class Prediction {
    private static Random random = new Random(47);
    private boolean shadow = random.nextDouble() > 0.5;

    @Override
    public String toString() {
        if (shadow)
            return "Six more weeks of Winter!";
        else
            return "Early Spring";
    }
}

class SpringDetector {
    private static <T extends Groundhog> void detectSpring(Class<T> type) throws Exception {
        Constructor<T> ghog = type.getConstructor(int.class);
        Map<Groundhog, Prediction> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(ghog.newInstance(i), new Prediction());
        }
        System.out.println("map=" + map);
        Groundhog gh = ghog.newInstance(3);
        System.out.println("Looking up prediction for " + gh);
        if (map.containsKey(gh)) {
            System.out.println(map.get(gh));
        } else {
            System.out.println("Key not found:" + gh);
        }
    }

    public static void main(String[] args) throws Exception {
        detectSpring(Groundhog.class);
    }
}

public class c_17_9 {

}
