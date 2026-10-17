import java.util.ArrayList;
import java.util.List;

public class DispatchManager {

    private final City city;
    private final PathFinder pathFinder;
    private final List<EmergencyVehicle> vehicles;

    public DispatchManager(City city) {

        if (city == null) {
            throw new IllegalArgumentException("City cannot be null");
        }

        this.city = city;
        this.pathFinder = new PathFinder();
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(EmergencyVehicle vehicle) {

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        if (vehicle.getCurrentLocation() == null ||
                city.getLocation(vehicle.getCurrentLocation().getId())
                        != vehicle.getCurrentLocation()) {

            throw new IllegalArgumentException(
                "Vehicle location must belong to the city"
            );
        }

        for (EmergencyVehicle existing : vehicles) {

            if (existing.getId() == vehicle.getId()) {
                throw new IllegalArgumentException(
                    "Vehicle ID already exists"
                );
            }
        }

        vehicles.add(vehicle);
    }

    public void addAmbulance(Ambulance ambulance) {
        addVehicle(ambulance);
    }

    
    public EmergencyVehicle dispatch(Emergency emergency) {

        if (emergency == null) {
            throw new IllegalArgumentException("Emergency cannot be null");
        }

        if (emergency.isResolved()) {
            throw new IllegalArgumentException(
                "Emergency is already resolved"
            );
        }

        if (city.getLocation(emergency.getLocation().getId())
                != emergency.getLocation()) {

            throw new IllegalArgumentException(
                "Emergency location must belong to the city"
            );
        }

        EmergencyVehicle bestVehicle = null;
        double shortestTime = Double.POSITIVE_INFINITY;

        for (EmergencyVehicle vehicle : vehicles) {

            if (!vehicle.isAvailable()) {
                continue;
            }

            if (!vehicle.canHandle(emergency.getType())) {
                continue;
            }

            List<Location> route = pathFinder.findFastestPath(
                city,
                vehicle.getCurrentLocation(),
                emergency.getLocation()
            );

            if (route.isEmpty()) {
                continue;
            }

            double travelTime = calculateRouteTime(route);

            if (travelTime < shortestTime) {
                shortestTime = travelTime;
                bestVehicle = vehicle;
            }
        }

        if (bestVehicle == null) {

            System.out.println(
                "No available emergency vehicle can reach the emergency."
            );

            return null;
        }

        bestVehicle.setAvailable(false);

        System.out.println(
            "Vehicle dispatched: " + bestVehicle.getName()
        );

        System.out.println(
            "Emergency type: " + emergency.getType()
        );

        System.out.println(
            "Estimated travel time: "
                + shortestTime + " seconds"
        );

        return bestVehicle;
    }

    private double calculateRouteTime(List<Location> route) {

        double totalTime = 0.0;

        for (int i = 0; i < route.size() - 1; i++) {

            Location start = route.get(i);
            Location end = route.get(i + 1);

            double fastestRoadTime = Double.POSITIVE_INFINITY;

            for (Road road : city.getConnectedRoads(start.getId())) {

                if (road.connects(end)) {

                    fastestRoadTime = Math.min(
                        fastestRoadTime,
                        road.calculateTravelTime()
                    );
                }
            }

            totalTime += fastestRoadTime;
        }

        return totalTime;
    }
}