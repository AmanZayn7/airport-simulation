package asiaPacificAirport;

public class Passenger implements Runnable {
    private final int id;
    private final AirportHub airportHub;
    private final Plane plane;

    public Passenger(int id, AirportHub airportHub, Plane plane) {
        this.id = id;
        this.airportHub = airportHub;
        this.plane = plane;
    }

    @Override
    public void run() {
        try {
            airportHub.board(id);
            airportHub.disembark(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }
}
