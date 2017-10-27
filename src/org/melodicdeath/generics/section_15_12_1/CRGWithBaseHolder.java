package org.melodicdeath.generics.section_15_12_1;

/**
 * Created by zt.melody on 2017/10/27.
 */

class BasicHolder<T> {
    T element;

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    void f() {
        if (element == null) {
            System.out.println("element is null");
        } else
            System.out.println(element.getClass().getSimpleName());
    }
}

class SubType extends BasicHolder<SubType> {
}

class Other {
}

class BasicOther extends BasicHolder<Other> {
}

public class CRGWithBaseHolder {
    public static void main(String[] args) {
        //此时泛型基类变成一种其所有导出类的公共功能的模板
        SubType subType1 = new SubType();
        SubType subType2 = new SubType();
        subType1.setElement(subType2);
        subType1.f();

        BasicOther basicOther1 = new BasicOther();
        BasicOther basicOther2 = new BasicOther();
        basicOther1.setElement(new Other());
        basicOther1.f();
    }
}

//---------------
class SelfBounded<T extends SelfBounded<T>>{
    T element;

    T getElement() {
        return element;
    }

    SelfBounded<T> setElement(T element) {
        this.element = element;
        return this;
    }
}

//自限定所有的，就是要求在继承关系中，像下面这样使用这个类
class A extends SelfBounded<A>{}
class B extends SelfBounded<A>{}

class C extends SelfBounded<C>{
    C setAndGet(C arg){
        setElement(arg);
        return getElement();
    }
}

//可以移除自限定这个限制，所有的类仍旧可以编译，E因此也会变得可编译
class D{}
//编译错误 D没有继承SelfBounded<D>
//class E extends SelfBounded<D>{}

//可以编译，因此自限定惯用法不是可以强制执行的，如果它确实很重要，可以要求一个外部工具来确保
// 不会使用原生类型来替代参数化类型
class F extends SelfBounded{}