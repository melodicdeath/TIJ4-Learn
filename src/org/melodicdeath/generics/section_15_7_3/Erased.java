package org.melodicdeath.generics.section_15_7_3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zt.melody on 2017/10/23.
 */

/*
* List<String>、List<T> 擦除后的类型为 List。
List<String>[]、List<T>[] 擦除后的类型为 List[]。
List<? extends E>、List<? super E> 擦除后的类型为 List<E>。
List<T extends Serialzable & Cloneable> 擦除后类型为 List<Serializable>。
*/
public class Erased<T> {
    private static final int SIZE = 100;

    //擦除丢失了在泛型代码中执行某些操作的能力。任何在运行时需要知道确切类型的操作都将无法工作
    //这意味着需要显示传递类型的class对象，以便在类型表达式中使用它
    public static void f(Object arg) {
//        if(arg instanceof T)

        //在java中无法实现，一部分原因是因为擦除，另一部分原因是因为编译器无法验证T具有无参构造器。在c++和c#中没问题
        //替代方法类似上面的instanceof：使用类型标签(class对象),使用newInstance方法
//        T var = new T();

        //一般的解决方案是用arraylist替代，如代码ListOfGenerices
//        T[] array=new T[SIZE];
//        T[] array = (T) new Object[SIZE];
    }

    public static void main(String[] args) {
        //前面对使用instanceof的尝试失败了，如果引入类型标签，就可以转而使用相关动态方法
        ClassTypeCapture<Building> classTypeCapture1 = new ClassTypeCapture<>(Building.class);
        System.out.println(classTypeCapture1.f(new Building()));
        System.out.println(classTypeCapture1.f(new House()));

        ClassTypeCapture<House> classTypeCapture2 = new ClassTypeCapture<>(House.class);
        System.out.println(classTypeCapture2.f(new Building()));
        System.out.println(classTypeCapture2.f(new House()));

        ClassAsFactory<Employee> classAsFactory1 = new ClassAsFactory<>(Employee.class);
        System.out.println("Employee Factory succeeded");
        try {
            //将会失败，因为integer没有默认构造器。这个错误并不是在编译时被捕获，所有sum不赞成这种方式
            //建议用显示的工厂，并限制类型，使得只能接受实现了这个工厂的类
            ClassAsFactory<Integer> classAsFactory2 = new ClassAsFactory<>(Integer.class);
        } catch (Exception e) {
            System.out.println("Integer Factory faild");
        }

        new Foo<Integer>(new IngegerFactory());
        new Foo<Widget>(new Widget.Factory2());
    }
}

//------------------
class Building {
}

class House extends Building {
}

class ClassTypeCapture<T> {
    Class<T> clazz;

    public ClassTypeCapture(Class<T> clazz) {
        this.clazz = clazz;
    }

    boolean f(Object arg) {
        return clazz.isInstance(arg);
    }
}

//---------------
class ClassAsFactory<T> {
    T x;

    public ClassAsFactory(Class<T> clazz) {
        try {
            x = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Employee {
}

//----------------
interface Factory<T> {
    T create();
}

class Foo<T> {
    private T x;

    <U extends Factory<T>> Foo(U factory) {
        x = factory.create();
    }
}

class IngegerFactory implements Factory<Integer> {

    @Override
    public Integer create() {
        return new Integer(0);
    }
}

class Widget {
    public static class Factory2 implements Factory<Widget> {
        @Override
        public Widget create() {
            return new Widget();
        }
    }
}

//-----------
class ListOfGenerices<T> {
    private List<T> array = new ArrayList<>();

    public void add(T item) {
        array.add(item);
    }
    public T get(int index){
        return array.get(index);
    }

}