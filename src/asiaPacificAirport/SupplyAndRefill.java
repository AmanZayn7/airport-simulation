package asiaPacificAirport;

public class SupplyAndRefill implements Runnable {
    private final int planeId; // ID of the plane

    public SupplyAndRefill(int planeId) {
        this.planeId = planeId;
    }

    @Override
    public void run() {
        // Print a message indicating the start of supply refilling and cleaning
        System.out.println("Thread-SupplyAndRefill-Plane-" + planeId + ": Refilling supplies and cleaning...");
        try {
            // Simulate the time required for refilling supplies and cleaning with a sleep of 2 seconds
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Handle the interrupted exception if the thread is interrupted while sleeping
            e.printStackTrace();
        }
        // Print a message indicating the completion of supply refilling and cleaning
        System.out.println("Thread-SupplyAndRefill-Plane-" + planeId + ": Supplies refilled and cleaning completed.");
    }
}
