package org.melodicdeath.enumerated.section_19_11;

import java.util.Random;

/**
 * Created by zt.melody on 2017/10/30.
 */


interface Item {
    Outcome compete(Item item);

    Outcome eval(Paper paper);

    Outcome eval(Scissors scissors);

    Outcome eval(Rock rock);
}

class Paper implements Item {
    @Override
    public Outcome compete(Item item) {
        return item.eval(this);
    }

    @Override
    public Outcome eval(Paper paper) {
        return Outcome.DRAW;
    }

    @Override
    public Outcome eval(Scissors scissors) {
        return Outcome.WIN;
    }

    @Override
    public Outcome eval(Rock rock) {
        return Outcome.LOSE;
    }

    @Override
    public String toString() {
        return "Paper";
    }
}

class Scissors implements Item {
    @Override
    public Outcome compete(Item item) {
        return item.eval(this);
    }

    @Override
    public Outcome eval(Paper paper) {
        return Outcome.LOSE;
    }

    @Override
    public Outcome eval(Scissors scissors) {
        return Outcome.DRAW;
    }

    @Override
    public Outcome eval(Rock rock) {
        return Outcome.WIN;
    }

    @Override
    public String toString() {
        return "Scissors";
    }
}

class Rock implements Item {
    @Override
    public Outcome compete(Item item) {
        return item.eval(this);
    }

    @Override
    public Outcome eval(Paper paper) {
        return Outcome.WIN;
    }

    @Override
    public Outcome eval(Scissors scissors) {
        return Outcome.LOSE;
    }

    @Override
    public Outcome eval(Rock rock) {
        return Outcome.DRAW;
    }

    @Override
    public String toString() {
        return "Rock";
    }
}

public class RoshamBo1 {
    private static final int SIZE = 20;
    private static Random random = new Random(47);

    private static Item newItem() {
        switch (random.nextInt(3)) {
            default:
            case 0:
                return new Scissors();
            case 1:
                return new Paper();
            case 2:
                return new Rock();
        }
    }

    private static void match(Item a, Item b) {
        System.out.println(a + " vs. " + b + ": " + a.compete(b));
    }

    public static void main(String[] args) {
        for (int i = 0; i < SIZE; i++) {
            match(newItem(), newItem());
        }
    }
}

