package org.melodicdeath.generics.section_15_8_2;

import java.lang.reflect.Array;

/**
 * Created by zt.melody on 2017/10/24.
 */

class Generic<T> {

}

public class ArrayOfGeneric {
    static final int SIZE = 100;
    //永远都不能创建这个确切类型的数组，但可以定义泛型数组的引用
    static Generic<Integer>[] gia;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        //不能直接创建泛型数组
        //Generic<Integer> ga = new Generic<Integer>[];

        //java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Lorg.melodicdeath.generics.Generic;
        //数组在创建的时候是一个 Object 数组,转型将报错
        //gia = (Generic<Integer>[]) new Object[SIZE];

        //看上去像没转，但是多了编译器对参数的检查和自动转型，所以下面赋值将报错
        gia = (Generic<Integer>[]) new Generic[SIZE];
        gia[0] = new Generic<>();

        //Error:(27, 16) java: 不兼容的类型: java.lang.Object无法转换为org.melodicdeath.generics.Generic<java.lang.Integer>
//        gia[1] = new Object();
        //Error:(30, 18) java: 不兼容的类型: org.melodicdeath.generics.Generic<java.lang.Double>无法转换为org.melodicdeath.generics.Generic<java.lang.Integer>
//        gia[2] = new Generic<Double>();
    }
}

//---------------
class GenericArray<T> {
    private T[] array;

    @SuppressWarnings("unchecked")
    GenericArray(int sz) {
        array = (T[]) new Object[sz];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    public T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        GenericArray<Integer> gai = new GenericArray<>(10);
        //java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.Integer;
        //因为有擦除，实际运行时的类型只能是Object[],如果立即将其转型为T[]，在编译期该数组的实际类型就将丢失从而错过某些错误的类型检查。
        //最好是在集合内部使用Object[]，然后使用数组元素时再转型。
        //Integer[] ia = gai.rep();

        Object[] oa = gai.rep();
    }
}

//-----------------
class GenericArray2<T> {
    private Object[] array;

    @SuppressWarnings("unchecked")
    GenericArray2(int sz) {
        array = new Object[sz];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return (T) array[index];
    }

    public T[] rep() {
        return (T[]) array;
    }

    public static void main(String[] args) {
        GenericArray2<Integer> gai = new GenericArray2<>(10);
        for (int i = 0; i < 10; i++) {
            gai.put(i, i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.print(gai.get(i) + ",");
        }

        //这里还是将报错，还是尝试将Object[]转型为T[]，仍旧不正确，没有任何方式可以推翻底层的数据类型，它只能是Object
        //在内部将array作为Object[]而不是T[]的优势是：我们不太可能忘记这个数组的运行时类型，
        //从而引入不必要的缺陷，尽管这类缺陷可以在运行时迅速探测到
        //Integer[] ia = gai.rep();
    }
}

//------------------更好的方式是使用类型标记----------------
class GenericArrayWithTypeToken<T> {
    private T[] array;

    @SuppressWarnings("unchecked")
    GenericArrayWithTypeToken(Class<T> clazz, int sz) {
        array = (T[]) Array.newInstance(clazz, sz);
    }

    void put(int index, T item) {
        array[index] = item;
    }

    T get(int index) {
        return array[index];
    }

    T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        GenericArrayWithTypeToken<Integer> gai = new GenericArrayWithTypeToken<>(Integer.class,10);
        for (int i = 0; i < 10; i++) {
            gai.put(i, i);
        }

        Integer[] ia = gai.rep();
        for (int i : ia) {
            System.out.print(i + ",");
        }
    }
}

/*http://www.cnblogs.com/drizzlewithwind/p/6101081.html*/
