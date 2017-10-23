package org.melodicdeath.innerclass.section_1083;

/**
 * Created by zt.melody on 2017/10/22.
 */

class WithInner {
    class Inner {
    }
}

//集成内部类时，必须创建外围类对象
public class InheritInner extends WithInner.Inner {
    //wont complie
//    InheritInner(){}

    public InheritInner(WithInner withInner) {
        withInner.super();
    }

    public static void main(String[] args) {
        InheritInner inheritInner = new InheritInner(new WithInner());
    }
}
