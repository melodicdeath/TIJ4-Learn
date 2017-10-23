package org.melodicdeath.generics;

/**
 * Created by zt.melody on 2017/10/24.
 */

class Generic<T> {

}

public class ArrayOfGeneric {
    static final int SIZE = 100;
    //永远都不能创建这个确切类型的数组
    static Generic<Integer>[] gia;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        //java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Lorg.melodicdeath.generics.Generic;
        //gia = (Generic<Integer>[]) new Object[SIZE];

        gia = (Generic<Integer>[]) new Generic[SIZE];
        gia[0] = new Generic<>();

        //Error:(27, 16) java: 不兼容的类型: java.lang.Object无法转换为org.melodicdeath.generics.Generic<java.lang.Integer>
//        gia[1] = new Object();
        //Error:(30, 18) java: 不兼容的类型: org.melodicdeath.generics.Generic<java.lang.Double>无法转换为org.melodicdeath.generics.Generic<java.lang.Integer>
//        gia[2] = new Generic<Double>();
    }
}