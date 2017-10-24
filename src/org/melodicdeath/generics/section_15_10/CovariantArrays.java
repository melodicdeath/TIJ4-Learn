package org.melodicdeath.generics.section_15_10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zt.melody on 2017/10/24.
 */

class Fruit{}
class Apple extends Fruit{}
class Jonathan extends Apple{}
class Orange extends Fruit{}

public class CovariantArrays {
    public static void main(String[] args) {
        Fruit[] fruits = new Apple[10];
        fruits[0]=new Apple();
        fruits[1]=new Jonathan();

        //编译时允许向上转型，这对于编译器来说是有意义的。但在运行时数组机制知道它处理的是Apple[]，因此放置异构类型将抛出异常
        //数组对象可以保留有关它们包含的对象类型规则。就好像数组对它们持有的对象时有意识的。
        //对数组来说这种赋值不那么可怕，因为在运行时可以发现已经插入了不正确的类型
        //泛型的主要目标之一是将这种错误监测移入编译期
        try {
            fruits[2] = new Fruit();
        }catch (Exception e){
            System.out.println(e);
        }

        try {
            fruits[3] = new Orange();
        }
        catch (Exception e){
            System.out.println(e);
        }

        //编译失败
        //就算容器持有的类型之间有继承关系，容器之间是没有关系的。与数组不同，泛型没有內建的协变类型。数组在语言中是完全定义的，
        //因此可以內建了编译期和运行期的检查。而泛型在编译期会将类型擦除。
        //List<Fruit> fruits1 = new ArrayList<Apple>();

        //想在泛型之间建立持有对象的向上转型关系，只能使用通配符
        //Apple是Fruit的子类型，List<Apple> 是 List<? extends Fruit> 的子类型
        //List<? extends Fruit>限制必须持有Fruit及其子类，但编译器并不知道具体类型是什么,可以是Apple，也可以是Orange。
        //因为没法确定，为了类型安全不允许往里面添加任何类型，甚至Object也不行。
        //另一方面，编译器知道不管持有什么类型，至少是个Fruit，所以当读取时，能确保得到一个Fruit
        List<? extends Fruit> fruits2 = new ArrayList<Apple>();

        /*Error:(50, 16) java: 对于add(org.melodicdeath.generics.section_15_10.Apple), 找不到合适的方法
    方法 java.util.Collection.add(capture#1, 共 ? extends org.melodicdeath.generics.section_15_10.Fruit)不适用
      (参数不匹配; org.melodicdeath.generics.section_15_10.Apple无法转换为capture#1, 共 ? extends org.melodicdeath.generics.section_15_10.Fruit)
    方法 java.util.List.add(capture#1, 共 ? extends org.melodicdeath.generics.section_15_10.Fruit)不适用
      (参数不匹配; org.melodicdeath.generics.section_15_10.Apple无法转换为capture#1, 共 ? extends org.melodicdeath.generics.section_15_10.Fruit)*/
//        fruits2.add(new Apple());
//        fruits2.add(new Fruit());
//        fruits2.add(new Object());

        fruits2.add(null);
        Fruit f = fruits2.get(0);

        Apple apple = (Apple)fruits2.get(0);
        System.out.println(apple);

        //contains和indexOf的参数是Object，不涉及通配符，所以可以安全调用
        //而add的参数是? extends Fruit，在编译时会替换成capture#1，因此任何与任何类型都不会匹配。
        System.out.println(fruits2.contains(apple));
        System.out.println(fruits2.indexOf(apple));


        //fruits指向的是一个装有Apple的某种超类(supertype)的List。同样的，我们不知道究竟是什么超类。
        //但我们知道Fruit和任何Fruit的子类都跟它的类型兼容。因此可以写入
        //另一方面，当我们取出时，只能取出Object实例。因为我们不知道超类究竟是什么，编译器唯一能保证的只是它是个Object，因为Object是任何Java类型的超类。
        List<? super Fruit> fruit3 = new ArrayList<>();
        fruit3.add(new Apple());
        fruit3.add(new Orange());
        fruit3.add(new Jonathan());
        fruit3.add(new Fruit());

        for(Object fruit : fruit3){
            System.out.println((Fruit)fruit);
        }

        //即要读又要写，那就不要用通配符
        List<Fruit> fruit4 = new ArrayList<>();
        fruit4.add(new Apple());
        fruit4.add(new Orange());
        fruit4.add(new Jonathan());
        fruit4.add(new Fruit());

        for(Fruit fruit : fruit4){
            System.out.println(fruit);
        }
    }
}

class GenericReading {

    static <T> void writeExact(List<T> list,T item) {
        list.add(item);
    }

    static <T> void writeWithWildcard(List<? super T> list,T item){
        list.add(item);
    }

    public static void main(String[] args) {
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple());

        List<Fruit> fruits = new ArrayList<>();
        fruits.add(new Fruit());

        writeExact(apples,new Apple());
        writeExact(fruits,new Apple());//这里在jdk7以前应该会报错

        System.out.println(Arrays.toString(fruits.toArray()));

        writeWithWildcard(apples,new Apple());
        writeWithWildcard(fruits,new Apple());
    }
}

