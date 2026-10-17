public abstract class EmergencyVehicle {

    private final int id;
    private final String name;
    private Location currentLocation;
    private boolean available;

    public EmergencyVehicle(int id, String name, Location currentLocation) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Vehicle name cannot be empty");
        }

        if (currentLocation == null) {
            throw new IllegalArgumentException("Vehicle location cannot be null");
        }

        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.available = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setCurrentLocation(Location location) {

        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        this.currentLocation = location;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract boolean canHandle(EmergencyType type);

    @Override
    public String toString() {
        return name
            + " | ID: " + id
            + " | Location: " + currentLocation.getName()
            + " | Available: " + available;
    }
}