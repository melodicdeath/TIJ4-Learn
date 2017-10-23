package org.melodicdeath.generics.section_1514;

//泛型类型推导

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

class Pet {
}

class Person {
}

class New {
    public static <K, V> Map<K, V> map() {
        return new HashMap<K, V>();
    }
}

public class LimitsOfInterence {
    static void f(Map<Person, List<? extends Pet>> personPets) {
        System.out.println(Arrays.toString(personPets.getClass().getTypeParameters()));
    }

    public static void main(String[] args) {
        //传入参数已经jdk1.7以后已经可以推断
        f(New.map());
        f(New.<Person, List<? extends Pet>>map());

        //jdk8
        //String和ArrayList都实现了Serializable接口，编译器可以根据参数类型、目标类型和返回类型（如果有返回的话）进行推导
        Serializable s = pick("d", new ArrayList<String>());
        System.out.println(s);

        //类型推导与泛型类和非泛型类的泛型构造器
        MyClass<Integer> myClass = new MyClass<>("");
        System.out.println(myClass);

        //目标类型推导,List<String>就是目标类型
        List<String> listOne = Collections.emptyList();
        System.out.println(listOne);
        //jdk7以前会编译失败
        processStringList(Collections.emptyList());
    }

    static <T> List<T> emptyList(){return null;}
    static <T> T pick(T a1, T a2) { return a2; }

    static void processStringList(List<String> stringList) {
        // process stringList
    }



}

class MyClass<X> {
    <T> MyClass(T t) {
        // ...
    }
}
