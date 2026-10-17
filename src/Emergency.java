public class Emergency {

    private int id;
    private Location location;
    private boolean resolved;

    public Emergency(int id, Location location) {
        this.id = id;
        this.location = location;
        this.resolved = false;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void resolve() {
        resolved = true;
    }

    @Override
    public String toString() {
        return "Emergency " + id
            + " | Location: " + location.getName()
            + " | Resolved: " + resolved;
    }
}