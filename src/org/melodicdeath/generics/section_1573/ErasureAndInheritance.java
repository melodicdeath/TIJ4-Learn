package org.melodicdeath.generics.section_1573;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zt.melody on 2017/10/23.
 */
class GenericBase<T>{
    private T element;

    T getElement() {
        return element;
    }

    void setElement(T element) {
        this.element = element;
    }
}

class Derived1<T> extends GenericBase<T>{}

class Derived2<T> extends GenericBase<T>{}

public class ErasureAndInheritance {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Derived2 d2 = new Derived2();
        Object object = d2.getElement();
        d2.setElement(object);//没有提供泛型类型

        //3.泛型中所有动作都发生在边界处——对传进来的值进行进行额外的编译器检查、对传出的值进行转型
        Derived2<Integer> d1 = new Derived2<>();
        d1.setElement(1);

        ArrayMaker<String> arrayMaker = new ArrayMaker<>(String.class);
        String[] strings = arrayMaker.create(9);
        System.out.println(Arrays.toString(strings));

        FilledListMaker<String> filledListMaker=new FilledListMaker<>();
        List<String> list = filledListMaker.create("Hello",4);
        System.out.println(list);
    }
}

class ArrayMaker<T>{
    private Class<T> kind;

    ArrayMaker(Class<T> kind){
        this.kind=kind;
    }

    @SuppressWarnings("unchecked")
    T[] create(int size){
        //1.kind被擦除成class,因此当使用它时并未拥有类型信息。必须强制转型并且忽略警告。
        return (T[])Array.newInstance(kind,size);
    }
}

class FilledListMaker<T>{
    //2.尽管编译器无法知道T的任何信息，但是它仍旧可以在编译器确保放置到result中的对象具有T类型
    List<T> create(T t,int size){
        List<T> result = new ArrayList<>();
        for(int i=0;i<size;i++){
            result.add(t);
        }

        return result;
    }
}
