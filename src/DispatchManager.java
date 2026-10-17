import java.util.ArrayList;

public class DispatchManager {

    private City city;
    private PathFinder pathFinder;
    private ArrayList<Ambulance> ambulances;

    public DispatchManager(City city) {
        this.city = city;
        pathFinder = new PathFinder();
        ambulances = new ArrayList<>();
    }

    public void addAmbulance(Ambulance ambulance) {
        ambulances.add(ambulance);
    }

    public Ambulance dispatch(Emergency emergency) {

        Ambulance nearest = null;
        double shortestTime = Double.POSITIVE_INFINITY;

        for (Ambulance ambulance : ambulances) {

            if (ambulance.isAvailable()) {

                ArrayList<Location> route = new ArrayList<>(
                    pathFinder.findFastestPath(
                        city,
                        ambulance.getCurrentLocation(),
                        emergency.getLocation()
                    )
                );

                if (!route.isEmpty()) {

                    double time = calculateRouteTime(route);

                    if (time < shortestTime) {
                        shortestTime = time;
                        nearest = ambulance;
                    }
                }
            }
        }

        if (nearest != null) {
            nearest.setAvailable(false);

            System.out.println("Ambulance dispatched: " + nearest.getName());
            System.out.println("Travel time: " + shortestTime + " seconds");

        } else {
            System.out.println("No ambulance available.");
        }

        return nearest;
    }

    private double calculateRouteTime(ArrayList<Location> route) {

        double total = 0;

        for (int i = 0; i < route.size() - 1; i++) {

            Location start = route.get(i);
            Location end = route.get(i + 1);

            double bestTime = Double.POSITIVE_INFINITY;

            for (Road road : city.getConnectedRoads(start.getId())) {

                if (!road.isBlocked() && road.connects(end)) {

                    if (road.calculateTravelTime() < bestTime) {
                        bestTime = road.calculateTravelTime();
                    }
                }
            }

            total += bestTime;
        }

        return total;
    }
}