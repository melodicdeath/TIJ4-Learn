package org.melodiceath.innerclass.section_1081;

interface Incrementbale {
    void increment();
}

class Callee1 implements Incrementbale {
    private int i = 0;

    @Override
    public void increment() {
        i++;
        System.out.println("callee1:" + i);
    }
}

class MyIncrement {
    public void increment() {
        System.out.println("Other operation");
    }

    static void f(MyIncrement myIncrement) {
        myIncrement.increment();
    }
}

class Callee2 extends MyIncrement {
    private int i = 0;

    @Override
    public void increment() {
        super.increment();
        i++;
        System.out.println("callee2:" + i);
    }

    private class Closure implements Incrementbale {

        @Override
        public void increment() {
            System.out.print("Closure:\t");
            Callee2.this.increment();
        }
    }

    public Incrementbale getCallbackReference(){
        return new Closure();
    }
}

class Caller {
    private Incrementbale callbackReference;

    Caller(Incrementbale incrementbale) {
        callbackReference = incrementbale;
    }

    public void go(){
        callbackReference.increment();
    }
}

public class Callbacks{
    public static void main(String[] args) {
        Callee1 callee1 = new Callee1();
        Callee2 callee2 = new Callee2();

        MyIncrement.f(callee2);

        Caller caller1 = new Caller(callee1);
        Caller caller2 = new Caller(callee2.getCallbackReference());

        caller1.go();
        caller1.go();

        caller2.go();
        caller2.go();
    }
}
