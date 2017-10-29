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
    // 的排序状态，然后使用Collections.binarySearch进行查询，这不是最优的方式。
    // 散列的价值在于速度：散列使得查询得以快速进行。它将键保存在某持，以便能够很快找到。
    // 存储一组元素最快的方式是数组，但是数组有容量限制。解决方法是数组并不保存键本身，而是保存键的信息。
    // 通过键生成生成一个数字，即hashCode方法生成的散列码作为数组的下标。为了解决容量问题，可以让不同的键生成相同下标（通过模运算，
    // 比如限制数组大小为997，用散列码与1000取模，生成的下标会在997以内。与97取模，下标则在97以内。为使散列分布均匀，数量通常为质数。
    // 无法控制下标的产生。这个值依赖于具体HashMap对象的容量，而容量的改变与容器的充满程度和负载因子（默认0.75，表示
    // 当容器负载情况达到负载因子水平时，自动增加容量）有关。），
    // 也就是说有可能会产生冲突。因此数组大小就不重要了，任何键总能找到它的位置。
    // 于是查询一个值首先就是计算散列码，然后利用散列码查询数组。如果能保证没有冲突（值的数量是固定的，那么就有可能），那可就拥有
    // 一个完美的散列函数。但是这种情况只是特例。通常这种冲突由外部处理：数组并不直接保存值，而是保存值的List。然后对List中的
    // 值使用equals进行线性查询。这部分查询自然会比较慢。但是如果散列函数好的话，数组的每个位置就只有少量的值。因此不用查询整个List，
    // 而是快速跳到数组的某个位置，对少量元素进行比较就能得到其值。这边是HashMap速度快的原因。
    /*
    * 正确的equals必须满足5个条件：
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
    * 设计hashCode时最重要的因素就是：无论何时，对同一个对象调用hashCode都应该生成同样的值。如果在一个对象用Put
    * 添加进hashMap产生一个hashCode值，而用get取出时却产生了另外一个hashCode值，那么就无法重新获取该对象了。
    * 所以，如果hashCode方法依赖于对象中易变的数据，就要当心，因为当此数据变化时，hashCode会生成一个不同的散列码，
    * 相当于产生了一个不同的键。
    * 此外，也不应该使hashCode依赖于具有唯一性的对象信息，尤其是使用this的值。默认的hashCode使用的对象地址。
    * 所以应该使用对象内有意义的识别信息。
    * 要想使hashCode实用，它必须速度快并且必须有意义。也就是说它必须基于对象内容生成散列码。散列码不必是独一无二，
    * 应该更关注生成速度而不是唯一性，但是通过hashCode和equals必须能够完全确定对象的身份。
    * 生成下标前，hashCode还需要做进一步处理，所以散列码的生成范围并不重要，只要是int即可。
    * 好的hashCode应该产生分布均匀的散列码。如果在一块，那么hashMap或者hashSet某些区域的负载会很重，这样就不如分布均匀
    * 的散列函数快。
    *
    * 基本指导
    * 1)给int变量result赋予某个非零值常量，如17.
    * 2)为对象内每个有意义的域f，即每个可以做equals操作的域，计算出一个int散列码。具体计算方式见OneNote-技术-java-集合
    * 3)合并计算得到的散列码：result=37*result+c
    * 4)返回result
    * 5)检查
    *
    * 例子：
    * 计算name和id的散列码：
    * 1)可以用name.hashCode*id
    * 2)
    * int result=17;
    * result=37*result+name.hashCode();
    * result=37*result+id;
    * return result;
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
