package org.melodicdeath.enumerated.section_19_11;

import java.util.EnumMap;

public enum RoshamBo5 implements Competitor<RoshamBo5> {
    ROCK, SCISSORS, PAPER;

    static EnumMap<RoshamBo5, EnumMap<RoshamBo5, Outcome>> table = new EnumMap<>(RoshamBo5.class);

    static {
        for (RoshamBo5 roshamBo5 : values()) {
            table.put(roshamBo5, new EnumMap<>(RoshamBo5.class));
        }

        initRows(PAPER, Outcome.DRAW, Outcome.LOSE, Outcome.WIN);
        initRows(SCISSORS, Outcome.WIN, Outcome.DRAW, Outcome.LOSE);
        initRows(ROCK, Outcome.LOSE, Outcome.WIN, Outcome.DRAW);
    }

    static void initRows(RoshamBo5 roshamBo5, Outcome vsPaperResult, Outcome vsScissorsResult, Outcome vsRockResult) {
        EnumMap<RoshamBo5, Outcome> enumMap = table.get(roshamBo5);
        enumMap.put(PAPER, vsPaperResult);
        enumMap.put(SCISSORS, vsScissorsResult);
        enumMap.put(ROCK, vsRockResult);
    }

    @Override
    public Outcome compete(RoshamBo5 competitor) {
        return table.get(this).get(competitor);
    }

    public static void main(String[] args) {
        RoshamBo.play(RoshamBo5.class, 20);
    }
}

