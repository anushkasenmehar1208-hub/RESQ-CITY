import java.util.ArrayList;
import java.util.HashMap;

public class City {

    private String name;
    private HashMap<Integer, Location> locations;
    private HashMap<Integer, Road> roads;
    private HashMap<Integer, ArrayList<Road>> connectedRoads;

    public City(String name) {
        this.name = name;
        locations = new HashMap<>();
        roads = new HashMap<>();
        connectedRoads = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addLocation(Location location) {
        int id = location.getId();

        locations.put(id, location);
        connectedRoads.put(id, new ArrayList<>());
    }

    public void addRoad(Road road) {
        int startId = road.getStart().getId();
        int endId = road.getEnd().getId();

        roads.put(road.getId(), road);

        connectedRoads.get(startId).add(road);
        connectedRoads.get(endId).add(road);
    }

    public Location getLocation(int id) {
        return locations.get(id);
    }

    public Road getRoad(int id) {
        return roads.get(id);
    }

    public ArrayList<Location> getAllLocations() {
        return new ArrayList<>(locations.values());
    }

    public ArrayList<Road> getAllRoads() {
        return new ArrayList<>(roads.values());
    }

    public ArrayList<Road> getConnectedRoads(int locationId) {
        return new ArrayList<>(connectedRoads.get(locationId));
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