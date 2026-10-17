public class Emergency {

    private final int id;
    private final EmergencyType type;
    private final Location location;
    private final int severity;

    private boolean resolved;

    public Emergency(int id, EmergencyType type, Location location, int severity) {

        if (type == null) {
            throw new IllegalArgumentException("Emergency type cannot be null");
        }

        if (location == null) {
            throw new IllegalArgumentException("Emergency location cannot be null");
        }

        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException("Severity must be between 1 and 5");
        }

        this.id = id;
        this.type = type;
        this.location = location;
        this.severity = severity;

        this.resolved = false;
    }

    public int getId() {
        return id;
    }

    public EmergencyType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public int getSeverity() {
        return severity;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void resolve() {
        this.resolved = true;
    }

    @Override
    public String toString() {
        return "Emergency " + id
            + " | Type: " + type
            + " | Location: " + location.getName()
            + " | Severity: " + severity
            + " | Resolved: " + resolved;
    }
}