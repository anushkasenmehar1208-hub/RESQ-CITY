import java.util.List;

public class SimulationEngine {

    private final City city;
    private final PathFinder pathFinder;

    public SimulationEngine(City city) {

        if (city == null) {
            throw new IllegalArgumentException("City cannot be null");
        }

        this.city = city;
        this.pathFinder = new PathFinder();
    }

    public void simulateAmbulance(
            Ambulance ambulance, Emergency emergency) {

        simulateVehicle(ambulance, emergency);
    }

    public void simulateVehicle(
            EmergencyVehicle vehicle, Emergency emergency) {

        if (vehicle == null || emergency == null) {
            throw new IllegalArgumentException(
                "Vehicle and emergency cannot be null"
            );
        }

        if (!vehicle.canHandle(emergency.getType())) {
            throw new IllegalArgumentException(
                "Vehicle cannot handle this emergency type"
            );
        }

        if (emergency.isResolved()) {
            throw new IllegalArgumentException(
                "Emergency is already resolved"
            );
        }

        List<Location> route = pathFinder.findFastestPath(
            city,
            vehicle.getCurrentLocation(),
            emergency.getLocation()
        );

        if (route.isEmpty()) {
            System.out.println("No route available!");
            return;
        }

        System.out.println("\n--- EMERGENCY VEHICLE MOVEMENT ---");

        double totalTravelTime = 0.0;

        for (int i = 0; i < route.size() - 1; i++) {

            Location current = route.get(i);
            Location next = route.get(i + 1);

            Road selectedRoad = null;
            double fastestTime = Double.POSITIVE_INFINITY;

            for (Road road : city.getConnectedRoads(current.getId())) {

                if (road.connects(next)) {

                    double travelTime = road.calculateTravelTime();

                    if (travelTime < fastestTime) {
                        fastestTime = travelTime;
                        selectedRoad = road;
                    }
                }
            }

            if (selectedRoad == null) {
                System.out.println(
                    "Road unavailable. Simulation stopped."
                );
                return;
            }

            totalTravelTime += fastestTime;

            vehicle.setCurrentLocation(next);

            System.out.println(
                vehicle.getName()
                + " moved to " + next.getName()
                + " | Travel time: "
                + fastestTime + " seconds"
            );
        }

        System.out.println(
            "\n" + vehicle.getName()
            + " arrived at "
            + emergency.getLocation().getName()
        );

        System.out.println(
            "Total travel time: "
            + totalTravelTime + " seconds"
        );

        emergency.resolve();
        vehicle.setAvailable(true);

        System.out.println("Emergency resolved successfully!");

        System.out.println(
            vehicle.getName()
            + " available again: "
            + vehicle.isAvailable()
        );
    }
}