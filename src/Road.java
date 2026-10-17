public class Road {

    private final int id;
    private final Location start;
    private final Location end;
    private final double distance;
    private final double speedLimit;

    private boolean blocked;
    private double trafficMultiplier;

    public Road(int id, Location start, Location end,
                double distance, double speedLimit) {

        if (start == null || end == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }

        if (start == end || start.getId() == end.getId()) {
            throw new IllegalArgumentException("A road must connect two different locations");
        }

        if (distance <= 0 || !Double.isFinite(distance)) {
            throw new IllegalArgumentException("Distance must be positive and finite");
        }

        if (speedLimit <= 0 || !Double.isFinite(speedLimit)) {
            throw new IllegalArgumentException("Speed limit must be positive and finite");
        }

        this.id = id;
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.speedLimit = speedLimit;

        this.blocked = false;
        this.trafficMultiplier = 1.0;
    }

    public int getId() {
        return id;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public double getDistance() {
        return distance;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public double getTrafficMultiplier() {
        return trafficMultiplier;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setTrafficMultiplier(double trafficMultiplier) {

        if (trafficMultiplier < 1.0 || !Double.isFinite(trafficMultiplier)) {
            throw new IllegalArgumentException(
                "Traffic multiplier must be finite and at least 1.0"
            );
        }

        this.trafficMultiplier = trafficMultiplier;
    }

    public double calculateTravelTime() {

        if (blocked) {
            return Double.POSITIVE_INFINITY;
        }

        return (distance / speedLimit) * 3600 * trafficMultiplier;
    }

    public boolean connects(Location location) {
        if (location == null) {
            return false;
        }

        return start.getId() == location.getId()
            || end.getId() == location.getId();
    } 

    public Location getOtherEnd(Location location) {

        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        if (start.getId() == location.getId()) {
            return end;
        }

        if (end.getId() == location.getId()) {
            return start;
        }

        throw new IllegalArgumentException(
            "Location is not connected to this road"
        );
    }

    @Override
    public String toString() {
        return "Road " + id
            + ": " + start.getName()
            + " <-> " + end.getName()
            + " | Distance: " + distance + " km"
            + " | Speed: " + speedLimit + " km/h"
            + " | Blocked: " + blocked;
    } 
}