package asiaPacificAirport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class SimulationMain {
    public static void main(String[] args) throws InterruptedException {
        AirportHub airportHub = new AirportHub();
        List<Thread> planeThreads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(7); // Adjusted for the emergency plane

        Random rand = new Random();

        // Normal planes
        for (int i = 1; i <= 5; i++) {
            Thread.sleep(rand.nextInt(2000)); // New airplane arrives every 0, 1, or 2 seconds
            Plane plane = new Plane(i, airportHub, latch);
            Thread planeThread = new Thread(plane);
            planeThreads.add(planeThread);
            planeThread.start();
        }

        // Emergency plane
        Thread.sleep(rand.nextInt(2000)); // Random arrival time for emergency plane
        Plane emergencyPlane = new Plane(6, airportHub, latch, true); // Emergency plane
        Thread emergencyThread = new Thread(emergencyPlane);
        planeThreads.add(emergencyThread);
        emergencyThread.start();

        // Wait for all plane threads to complete
        for (Thread thread : planeThreads) {
            thread.join();
        }

        // After all planes have left and gates are empty, print statistics
        airportHub.printStatistics();

        // Exit the program
        System.exit(0);
    }
}
