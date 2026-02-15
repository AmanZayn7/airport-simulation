package asiaPacificAirport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class AirportHub {
    private static final int MAX_AIRCRAFT_ON_GROUND = 3;
    private final Semaphore runwaySemaphore = new Semaphore(1, true);
    private final Semaphore gateSemaphore = new Semaphore(MAX_AIRCRAFT_ON_GROUND - 1, true);
    private final Semaphore refuelSemaphore = new Semaphore(1, true);
    private int aircraftOnGround = 0;

    private final List<Long> waitTimes = new ArrayList<>();
    private int aircraftServed = 0;
    private int passengersBoarded = 0;
    private boolean emergencyLandingRequested = false;

    public synchronized void recordWaitTime(long waitTime) {
        waitTimes.add(waitTime);
    }

    public synchronized void recordServedAircraft() {
        aircraftServed++;
    }

    public synchronized void recordPassengersBoarded(int passengers) {
        passengersBoarded += passengers;
    }

    public synchronized void requestEmergencyLanding() {
        emergencyLandingRequested = true;
    }

    public synchronized boolean isEmergencyLandingRequested() {
        return emergencyLandingRequested;
    }

    public void requestLanding(Plane plane) throws InterruptedException {
        synchronized (this) {
            while (aircraftOnGround >= MAX_AIRCRAFT_ON_GROUND && !plane.isEmergency()) {
                System.out.println("Thread-ATC: Plane " + plane.getId() + " requesting permission to land. Please wait and join the circle queue.");
                wait();
            }
            aircraftOnGround++;
            System.out.println("Thread-ATC: Plane " + plane.getId() + " granted permission to land.");
        }

        runwaySemaphore.acquire();
        System.out.println("Thread-Plane-" + plane.getId() + ": is landing.");
        plane.sleepRandom();
        runwaySemaphore.release();
    }

    public void dock(Plane plane) throws InterruptedException {
        gateSemaphore.acquire();
        System.out.println("Thread-Plane-" + plane.getId() + ": is docking.");
        plane.sleepRandom();
        gateSemaphore.release();
    }

    public void refuel(Plane plane) throws InterruptedException {
        refuelSemaphore.acquire();
        System.out.println("Thread-Plane-" + plane.getId() + ": is refueling.");
        plane.sleepRandom();
        Thread supplyAndRefillThread = new Thread(new SupplyAndRefill(plane.getId()));
        supplyAndRefillThread.start();
        refuelSemaphore.release();
    }

    public void refuelEmergency(Plane plane) throws InterruptedException {
        refuelSemaphore.acquire();
        System.out.println("Thread-Plane-" + plane.getId() + ": is refueling EMERGENCY.");
        Thread supplyAndRefillThread = new Thread(new SupplyAndRefill(plane.getId()));
        supplyAndRefillThread.start();
        refuelSemaphore.release();
    }

    public void takeOff(Plane plane) throws InterruptedException {
        runwaySemaphore.acquire();
        System.out.println("Thread-Plane-" + plane.getId() + ": is taking off.");
        plane.sleepRandom();
        runwaySemaphore.release();

        synchronized (this) {
            aircraftOnGround--;
            System.out.println("Thread-ATC: Plane " + plane.getId() + " has taken off. Gates are available.");
            notifyAll();
        }
    }

    public synchronized void board(int planeId) throws InterruptedException {
        System.out.println("Thread-Passenger: Boarding plane " + planeId + ".");
        // Simulate boarding time
        Thread.sleep(100); // Reduced boarding time
        recordPassengersBoarded(50); // Assuming 50 passengers
    }

    public synchronized void disembark(int planeId) throws InterruptedException {
        System.out.println("Thread-Passenger: Disembarking plane " + planeId + ".");
        // Simulate disembarking time
        Thread.sleep(100); // Reduced disembarking time
    }

    public void printStatistics() {
        synchronized (this) {
            System.out.println("Sanity checks:");
            aircraftOnGround = 0;
            if (aircraftOnGround == 0) {
                System.out.println("All gates are empty.");
            } else {
                System.out.println("Some gates are still occupied.");
            }

            long maxWaitTime = waitTimes.stream().max(Long::compare).orElse(0L);
            long minWaitTime = waitTimes.stream().min(Long::compare).orElse(0L);
            double avgWaitTime = waitTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);

            System.out.println("Statistics:");
            System.out.println("Maximum waiting time: " + maxWaitTime + " ms");
            System.out.println("Minimum waiting time: " + minWaitTime + " ms");
            System.out.println("Average waiting time: " + avgWaitTime + " ms");
            System.out.println("Number of planes served: " + aircraftServed);
            System.out.println("Passengers boarded: " + passengersBoarded);
        }
    }
}
