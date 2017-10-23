package org.melodicdeath.innerclass.section_1082;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<Event> eventList = new ArrayList<>();

    public void addEvent(Event event) {
        eventList.add(event);
    }

    public void run() {
        while (!eventList.isEmpty()) {
            for (Event event : new ArrayList<>(eventList)) {
                if(event.ready()){
                    System.out.println(event);
                    event.action();
                    eventList.remove(event);
                }
            }
        }
    }
}
