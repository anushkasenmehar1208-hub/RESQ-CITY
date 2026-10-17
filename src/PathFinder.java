import java.util.ArrayList;
import java.util.HashMap;

public class PathFinder {

    public ArrayList<Location> findFastestPath(City city, Location start, Location destination) {

        HashMap<Location, Double> times = new HashMap<>();
        HashMap<Location, Location> previous = new HashMap<>();
        ArrayList<Location> visited = new ArrayList<>();
        ArrayList<Location> path = new ArrayList<>();

        for (Location place : city.getAllLocations()) {
            times.put(place, Double.POSITIVE_INFINITY);
        }

        times.put(start, 0.0);

        while (true) {

            Location current = null;
            double shortest = Double.POSITIVE_INFINITY;

            for (Location place : city.getAllLocations()) {

                if (!visited.contains(place) && times.get(place) < shortest) {
                    shortest = times.get(place);
                    current = place;
                }
            }

            if (current == null || current == destination) {
                break;
            }

            visited.add(current);

            for (Road road : city.getConnectedRoads(current.getId())) {

                if (!road.isBlocked()) {

                    Location next = road.getOtherEnd(current);
                    double newTime = times.get(current) + road.calculateTravelTime();

                    if (newTime < times.get(next)) {
                        times.put(next, newTime);
                        previous.put(next, current);
                    }
                }
            }
        }

        if (times.get(destination) == Double.POSITIVE_INFINITY) {
            return path;
        }

        Location place = destination;

        while (place != null) {
            path.add(0, place);
            place = previous.get(place);
        }

        return path;
    }
}