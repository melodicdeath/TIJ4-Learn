package org.melodicdeath.enumerated.section_19_11;


public enum RoshamBo3 implements Competitor<RoshamBo3> {
    PAPER {
        @Override
        public Outcome compete(RoshamBo3 competitor) {
            switch (competitor) {
                default:
                case PAPER:
                    return Outcome.DRAW;
                case SCISSORS:
                    return Outcome.LOSE;
                case ROCK:
                    return Outcome.WIN;
            }
        }
    },
    SCISSORS {
        @Override
        public Outcome compete(RoshamBo3 roshamBo3) {
            switch (roshamBo3) {
                default:
                case PAPER:
                    return Outcome.WIN;
                case SCISSORS:
                    return Outcome.DRAW;
                case ROCK:
                    return Outcome.LOSE;
            }
        }
    },
    ROCK {
        @Override
        public Outcome compete(RoshamBo3 roshamBo3) {
            switch (roshamBo3) {
                default:
                case PAPER:
                    return Outcome.LOSE;
                case SCISSORS:
                    return Outcome.WIN;
                case ROCK:
                    return Outcome.DRAW;
            }
        }
    };

    @Override
    public abstract Outcome compete(RoshamBo3 roshamBo3);

    public static void main(String[] args) {
        RoshamBo.play(RoshamBo3.class, 20);
    }
}
