import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class City { 

    private final String name;

    private final Map<Integer, Location> locations;
    private final Map<Integer, Road> roads;

    private final Map<Integer, List<Road>> adjacencyList;

    public City(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name cannot be empty");

        }
        

        this.name = name;

        this.locations = new HashMap<>();
        this.roads = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addLocation(Location location) {

        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        int id = location.getId();

        if (locations.containsKey(id)) {
            throw new IllegalArgumentException(
                "Location ID already exists: " + id
            );
        }

        locations.put(id, location);
        adjacencyList.put(id, new ArrayList<>());
    }

    public void addRoad(Road road) {

        if (road == null) {
            throw new IllegalArgumentException("Road cannot be null");
        }

        if (roads.containsKey(road.getId())) {
            throw new IllegalArgumentException(
                "Road ID already exists: " + road.getId()
            );
        }

        int startId = road.getStart().getId();
        int endId = road.getEnd().getId();

        if (!locations.containsKey(startId)
                || !locations.containsKey(endId)) {
            throw new IllegalArgumentException(
                "Both road locations must exist in the city"
            );
        }

        if (locations.get(startId) != road.getStart() 
                || locations.get(endId) != road.getEnd()) {
            throw new IllegalArgumentException(
                "Road must reference the city's registered Location objects"
            );
        }

        roads.put(road.getId(), road);

        adjacencyList.get(startId).add(road);
        adjacencyList.get(endId).add(road);
    } 

    public Location getLocation(int id) {
        return locations.get(id);
    } 

    public Road getRoad(int id) {
        return roads.get(id);
    }

    public List<Location> getAllLocations() {
        return Collections.unmodifiableList(
            new ArrayList<>(locations.values())
        );
    }

    public List<Road> getAllRoads() {
        return Collections.unmodifiableList(
            new ArrayList<>(roads.values())
        );
    }

    public List<Road> getConnectedRoads(int locationId) {

        if (!locations.containsKey(locationId)) {
            throw new IllegalArgumentException(
                "Location does not exist: " + locationId
            );
        }

        return Collections.unmodifiableList(
            new ArrayList<>(adjacencyList.get(locationId))
        ); 
    }

    public int getLocationCount() {
        return locations.size();
    }

    public int getRoadCount() {
        return roads.size();
    }

    @Override
    public String toString() {
        return "City: " + name
            + " | Locations: " + getLocationCount()
            + " | Roads: " + getRoadCount();
    }
}