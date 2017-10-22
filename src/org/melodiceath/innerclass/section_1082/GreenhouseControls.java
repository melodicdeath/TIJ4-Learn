package org.melodiceath.innerclass.section_1082;

public class GreenhouseControls extends Controller {
    private boolean light = false;

    public class LightOn extends Event {
        public LightOn(long delayime) {
            super(delayime);
        }

        @Override
        public void action() {
            light = true;
        }

        @Override
        public String toString() {
            return "Light is on";
        }
    }

    public class LightOff extends Event {
        public LightOff(long delayime) {
            super(delayime);
        }

        @Override
        public void action() {
            light = false;
        }

        @Override
        public String toString() {
            return "Light is off";
        }
    }

    private boolean water = false;

    public class WaterOn extends Event {

        public WaterOn(long delayime) {
            super(delayime);
        }

        @Override
        public void action() {
            water = true;
        }

        @Override
        public String toString() {
            return "water is on";
        }
    }

    public class WaterOff extends Event {

        public WaterOff(long delayime) {
            super(delayime);
        }

        @Override
        public void action() {
            water = false;
        }

        @Override
        public String toString() {
            return "water is off";
        }
    }

    private String thermostat = "Day";

    public class ThermostatNight extends Event {

        public ThermostatNight(long delayime) {
            super(delayime);
        }

        @Override
        public void action() {
            thermostat = "Night";
        }

        @Override
        public String toString() {
            return "thermostat on night settings";
        }
    }

    public class ThermostatDay extends Event {

        public ThermostatDay(long delayime) {
            super(delayime);
        }

        @Override
        public void action() {
            thermostat = "Night";
        }

        @Override
        public String toString() {
            return "thermostat on day settings";
        }
    }

    public class Bell extends Event {

        public Bell(long delayime) {
            super(delayime);
        }

        @Override
        public void action() {
            addEvent(new Bell(delayime));
        }

        @Override
        public String toString() {
            return "Bing!";
        }
    }

    public class Restart extends Event {
        private Event[] events;

        public Restart(long delayime, Event[] events) {
            super(delayime);
            this.events = events;

            for (Event event : events) {
                addEvent(event);
            }
        }

        @Override
        public void action() {
            for (Event event : events) {
                event.start();
                addEvent(event);
            }
            start();
            addEvent(this);
        }

        @Override
        public String toString() {
            return "Restarting system";
        }
    }

    public static class Terminate extends Event {
        public Terminate(long delayTime) {
            super(delayTime);
        }

        @Override
        public void action() {
            System.exit(0);
        }

        @Override
        public String toString() {
            return "Terminate";
        }
    }

    public static void main(String[] args) {
        GreenhouseControls greenhouseControls = new GreenhouseControls();
        greenhouseControls.addEvent(greenhouseControls.new Bell(900));

        Event[] events = {
                greenhouseControls.new ThermostatNight(0),
                greenhouseControls.new LightOn(200),
                greenhouseControls.new LightOff(400),
                greenhouseControls.new WaterOn(600),
                greenhouseControls.new WaterOff(800),
                greenhouseControls.new ThermostatDay(1400)
        };

        greenhouseControls.addEvent(greenhouseControls.new Restart(2000,events));
        greenhouseControls.addEvent(new GreenhouseControls.Terminate(5000));
        greenhouseControls.run();
    }
}
