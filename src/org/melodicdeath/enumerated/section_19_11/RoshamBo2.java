package org.melodicdeath.enumerated.section_19_11;

/**
 * Created by zt.melody on 2017/10/31.
 */

interface Competitor<T extends Competitor<T>> {
    Outcome compete(T competitor);
}

public enum RoshamBo2 implements Competitor<RoshamBo2> {
    PAPER(Outcome.DRAW, Outcome.LOSE, Outcome.WIN),
    SCISSORS(Outcome.WIN, Outcome.DRAW, Outcome.LOSE),
    ROCK(Outcome.LOSE, Outcome.WIN, Outcome.DRAW);

    private Outcome vsPaperResult, vsScissorsResult, vsRockResult;

    RoshamBo2(Outcome vsPaperResult, Outcome vsScissorsResult, Outcome vsRockResult) {
        this.vsPaperResult = vsPaperResult;
        this.vsScissorsResult = vsScissorsResult;
        this.vsRockResult = vsRockResult;
    }

    @Override
    public Outcome compete(RoshamBo2 competitor) {
        switch (competitor) {
            default:
            case PAPER:
                return vsPaperResult;
            case ROCK:
                return vsRockResult;
            case SCISSORS:
                return vsScissorsResult;
        }
    }
}

class RoshamBo {
    static <T extends Competitor<T>> void match(T a, T b) {
        System.out.println(a + " vs. " + b + ": " + a.compete(b));
    }

    static <T extends Enum<T> & Competitor<T>> void play(Class<T> rsbClass, int size) {
        for (int i = 0; i < size; i++) {
            match(Enums.random(rsbClass), Enums.random(rsbClass));
        }
    }

    public static void main(String[] args) {
        play(RoshamBo2.class,20);
    }
}
