import java.util.ArrayList;

public class SimulationEngine {

    private City city;
    private PathFinder pathFinder;

    public SimulationEngine(City city) {
        this.city = city;
        pathFinder = new PathFinder();
    }

    public void simulateAmbulance(Ambulance ambulance, Emergency emergency) {

        ArrayList<Location> route = pathFinder.findFastestPath(
            city,
            ambulance.getCurrentLocation(),
            emergency.getLocation()
        );

        if (route.isEmpty()) {
            System.out.println("No route available!");
            return;
        }

        double totalTime = 0;

        for (int i = 0; i < route.size() - 1; i++) {

            Location start = route.get(i);
            Location end = route.get(i + 1);

            double roadTime = Double.POSITIVE_INFINITY;

            for (Road road : city.getConnectedRoads(start.getId())) {

                if (!road.isBlocked() && road.connects(end)) {

                    if (road.calculateTravelTime() < roadTime) {
                        roadTime = road.calculateTravelTime();
                    }
                }
            }

            totalTime += roadTime;

            ambulance.setCurrentLocation(end);

            System.out.println(
                ambulance.getName() + " moved to " + end.getName()
            );
        }

        System.out.println("Total travel time: " + totalTime + " seconds");

        emergency.resolve();
        ambulance.setAvailable(true);

        System.out.println("Emergency resolved!");
    }
}