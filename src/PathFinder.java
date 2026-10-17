import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class PathFinder {

    private static class QueueEntry {
        final Location location;
        final double travelTime;

        QueueEntry(Location location, double travelTime) {
            this.location = location;
            this.travelTime = travelTime;
        }
    }

    public List<Location> findFastestPath(
            City city, Location start, Location destination) {

        if (city == null || start == null || destination == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (city.getLocation(start.getId()) != start
                || city.getLocation(destination.getId()) != destination) {
            throw new IllegalArgumentException(
                "Locations must belong to the city"
            );
        }

        Map<Location, Double> distances = new HashMap<>();
        Map<Location, Location> previous = new HashMap<>();

        PriorityQueue<QueueEntry> queue = new PriorityQueue<>(
            Comparator.comparingDouble(entry -> entry.travelTime)
        );

        for (Location location : city.getAllLocations()) {
            distances.put(location, Double.POSITIVE_INFINITY);
        }

        distances.put(start, 0.0);
        queue.add(new QueueEntry(start, 0.0));

        while (!queue.isEmpty()) {

            QueueEntry entry = queue.poll();
            Location current = entry.location;

            if (entry.travelTime > distances.get(current)) {
                continue;
            }

            if (current == destination) {
                break;
            }

            for (Road road : city.getConnectedRoads(current.getId())) {

                if (road.isBlocked()) {
                    continue;
                }

                Location neighbor = road.getOtherEnd(current);

                double newDistance =
                    entry.travelTime + road.calculateTravelTime();

                if (newDistance < distances.get(neighbor)) {

                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current);

                    queue.add(new QueueEntry(neighbor, newDistance));
                }
            }
        }

        if (Double.isInfinite(distances.get(destination))) {
            return Collections.emptyList();
        }

        List<Location> path = new ArrayList<>();
        Location current = destination;

        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        return path;
    }
}