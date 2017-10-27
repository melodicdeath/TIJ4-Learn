package org.melodicdeath.generics.section_15_12_3;

/**
 * Created by zt.melody on 2017/10/28.
 */

/*参数协变
* 自限定类型的价值在于他们可以产生协变参数类型——方法参数类型随子类而变化。还可以产生于子类类型相同的返回类型
* */
class Base {
}

class Derived extends Base {
}

interface OrdinaryGetter {
    //Derived get();
    Base get();
}

interface DerivedGetter extends OrdinaryGetter {
    //将重写父类方法,子类方法能返回比基类方法更具体的类型。如果反过来则会编译失败。
    Derived get();
    //Base get();
}

public class CovariantReturnTypes {
    void test(DerivedGetter d) {
        Derived d2 = d.get();
    }
}

//-------------
interface GenericGetter<T extends GenericGetter<T>> {
    T get();
}

interface Getter extends GenericGetter<Getter> {

}

class GenericsAndReturnTypes {
    void test(Getter g) {
        Getter result = g.get();
        GenericGetter gg = g.get();
        //结果和上面一样
    }
}

//------------
//在非泛型代码中参数不能随子类变化
class OrdinaySetter {
    void set(Base base) {
        System.out.println("OrdinaySetter.set(Base)");
    }
}

class DerivedSetter extends OrdinaySetter {
    void set(Derived derived) {
        System.out.println("DerivedSetter.set(Derived)");
    }
}

class OrdinaryAuguments {
    public static void main(String[] args) {
        Base base = new Base();
        Derived derived = new Derived();
        DerivedSetter ds = new DerivedSetter();
        ds.set(derived);
        ds.set(base);
        //重载而不是重写，DerivedSetter。set()没有覆盖OrdinaySetter.set()
    }
}

//--------------
interface  SelfBoundSetter<T extends SelfBoundSetter<T>>{
    void set(T arg);
}

interface Setter extends SelfBoundSetter<Setter>{

}

class SelfBoundingAndCovariantAuguments{
    void test(Setter s1,Setter s2,SelfBoundSetter sbs){
        s1.set(s2);
        //编译失败，没有任何方法具有这样的签名，实际上这个参数已经被覆盖（重写）
//        s1.set(sbs);
    }
}

//----------------
class OrdinaySetter2<T> {
    void set(T base) {
        System.out.println("OrdinaySetter2.set(Base)");
    }
}

class DerivedSetter2 extends OrdinaySetter2<Base> {
    void set(Derived derived) {
        System.out.println("DerivedSetter2.set(Derived)");
    }
}

class OrdinaryAuguments2 {
    public static void main(String[] args) {
        Base base = new Base();
        Derived derived = new Derived();
        DerivedSetter2 ds = new DerivedSetter2();
        ds.set(derived);
        ds.set(base);
        //如果不使用自限定类型，普通继承机制就会介入，发生重载，就像在非泛型中那样
    }
}
