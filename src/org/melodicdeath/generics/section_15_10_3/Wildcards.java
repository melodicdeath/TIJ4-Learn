package org.melodicdeath.generics.section_15_10_3;

/**
 * Created by zt.melody on 2017/10/25.
 */

class Holder<T> {
    private T value;

    Holder() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        return value.equals(o);
    }
}

public class Wildcards {
    static void rawArgs(Holder holder, Object arg) {
        //warnning
        // 编译器知道holder是一个泛型类型，因此即使它在这里被表示成一个原生类型，编译器仍旧知道传递一个Object是不安全的，
        // 由于它是原生类型，可以将任何类型的对象传递给set，而这个对象将被向上转型为Object。
        // 因此无论何时，只要使用了原生类型，都会放弃编译器的检查
//        holder.setValue(arg);
//        holder.setValue(new Wildcards());

        Object object = holder.getValue();
    }

    static void unbondedArg(Holder<?> holder, Object arg) {
        //Error Holder<?>与Holder大致是相同的事物。不同的是Holder可以持有任何类型，而Holder<?>将持有某种具体类型的集合，
        // 因此不能只是向其中传递Object
        //List<?>列表的内容可以是任何类型，而且它与List<? extends Object>几乎相同。可以随时读取Object，但是不能向列表中写入内容。
//        holder.setValue(arg);
//        holder.setValue(new Wildcards());

        Object object = holder.getValue();
    }

    static <T> T exact1(Holder<T> holder) {
        T t = holder.getValue();
        return t;
    }

    static <T> T exact2(Holder<T> holder, T arg) {
        holder.setValue(arg);
        T t = holder.getValue();
        return t;
    }

    static <T> T wildSubtype(Holder<? extends T> holder, T arg) {
        //error
//        holder.setValue(arg);

        T t = holder.getValue();
        return t;
    }

    static <T> void wildSupertype(Holder<? super T> holder, T arg) {
        holder.setValue(arg);
        //error
//        T t = holder.getValue();
        Object object = holder.getValue();
    }

    public static void main(String[] args) {
        Holder raw = new Holder();
        Holder<Long> qualified = new Holder<>();
        Holder<?> unbounded = new Holder<Long>();
        Holder<? extends Long> bounded = new Holder<>();

        Long lng = 1L;

        //为了兼容，rawArgs接收所有Holder的不同变体，而不产生警告。但它在方法内部处理这些类型的方式不相同
        rawArgs(raw, lng);
        rawArgs(qualified, lng);
        rawArgs(unbounded, lng);
        rawArgs(bounded, lng);

        //向接收确切泛型类型（没有通配符）的方法传递一个原生Holder引用，会得到一个警告，因为确认的参数期望得到
        // 在原生类型中并不存在的信息，所以也不会有任何可以确定返回类型的类型信息
        Object r1 = exact1(raw);
        Long r2 = exact1(qualified);
        Object r3 = exact1(unbounded);
        Long r4 = exact1(bounded);


        Long r5 = exact2(raw, lng);
        Long r6 = exact2(qualified, lng);
        //<?>与<? extends T>都不允许写
//        Long r7 = exact2(unbounded, lng);
//        Long r8 = exact2(bounded,lng);

        //error
        Long r9 = wildSubtype(raw, lng);
        Long r10 = wildSubtype(qualified, lng);
        //只能返回Object
        Object r11 = wildSubtype(unbounded, lng);
        Long r12 = wildSubtype(bounded, lng);

        wildSupertype(raw,lng);
        wildSupertype(qualified,lng);

        //无法写入，感觉<? super T>和<T>的不同只在于前者限制读，后者读、写（都可以向泛型传递T及T的子类）都不限制
//        wildSupertype(unbounded,lng);
//        wildSupertype(bounded,lng);
    }

    /*可以看到exact2具有最多的限制，因为它希望精确的得到一个Holder<T>，以及一个具有类型的T参数。正因如此，它将产生错误
    和警告。有时这样做很好，但如果过于受限，那么就应该使用通配符，这取决于是否想要从泛型参数中返回类型确定的返回值，
    就像wildsubtype那样，或者想要向泛型参数传递类型确定的参数，就像wildSupertype中那样。
    使用确切类型代替通配符的好处是可以用泛型参数做更多的事，但是使用通配符使得你必须接受范围更宽的参数化类型作为参数，
    wildSubtype中，如果T是Fruit，那么holder可以是Holder<Apple>，为了防止将Orange放入Holder<Apple>中，对set的调用是不允许的。
    但是仍旧知道任何来自Holder<? extends Fruit>的对象至少是Fruit，因此get是允许的。
    因此必须逐个情况的权衡利弊，找到更合适的方法。*/
}
