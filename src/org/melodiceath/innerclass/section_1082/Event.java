package org.melodiceath.innerclass.section_1082;

public abstract class Event {
    private long eventTime;
    protected final long delayime;

    public Event(long delayime) {
        this.delayime = delayime;
        start();
    }

    public void start() {
        eventTime = System.nanoTime();
    }

    public boolean ready() {
        return System.nanoTime() >= eventTime;
    }

    public abstract void action();
}
