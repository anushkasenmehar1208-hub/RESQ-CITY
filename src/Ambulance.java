public class Ambulance {

    private int id;
    private String name;
    private Location location;
    private boolean available;

    public Ambulance(int id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.available = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return location;
    }

    public void setCurrentLocation(Location location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}