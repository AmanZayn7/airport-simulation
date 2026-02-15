package asiaPacificAirport;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Plane implements Runnable {
    private final int id;
    private final AirportHub airportHub;
    private final Random rand = new Random();
    private final CountDownLatch latch;
    private boolean emergency;

    public Plane(int id, AirportHub airportHub, CountDownLatch latch) {
        this.id = id;
        this.airportHub = airportHub;
        this.latch = latch;
        this.emergency = false;
    }

    // Constructor for emergency landing scenario
    public Plane(int id, AirportHub airportHub, CountDownLatch latch, boolean emergency) {
        this.id = id;
        this.airportHub = airportHub;
        this.latch = latch;
        this.emergency = emergency;
    }

    @Override
    public void run() {
        try {
            long startWaitTime = System.currentTimeMillis();
            if (emergency) {
                airportHub.requestEmergencyLanding();
                System.out.println("Thread-Plane-" + id + ": Requesting emergency landing.");
            } else {
                airportHub.requestLanding(this);
            }
            long endWaitTime = System.currentTimeMillis();
            airportHub.recordWaitTime(endWaitTime - startWaitTime);

            airportHub.dock(this);

            // Passengers disembarking
            airportHub.disembark(id);

            // Simulate refueling
            if (emergency) {
                airportHub.refuelEmergency(this);
            } else {
                airportHub.refuel(this);
            }

            // Passengers boarding
            airportHub.board(id);

            airportHub.takeOff(this);
            airportHub.recordServedAircraft();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    public int getId() {
        return id;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public void sleepRandom() throws InterruptedException {
        Thread.sleep(rand.nextInt(200)); // Reduced max sleep time to 200 ms
    }
}
