public class Road {

    private int id;
    private Location start;
    private Location end;
    private double distance;
    private double speedLimit;
    private boolean blocked;
    private double trafficMultiplier;

    public Road(int id, Location start, Location end, double distance, double speedLimit) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.speedLimit = speedLimit;
        blocked = false;
        trafficMultiplier = 1.0;
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
        this.trafficMultiplier = trafficMultiplier;
    }

    public double calculateTravelTime() {
        if (blocked) {
            return Double.POSITIVE_INFINITY;
        }

        return (distance / speedLimit) * 3600 * trafficMultiplier;
    }

    public boolean connects(Location location) {
        return start.getId() == location.getId()
            || end.getId() == location.getId();
    }

    public Location getOtherEnd(Location location) {
        if (start.getId() == location.getId()) {
            return end;
        }

        return start;
    }

    @Override
    public String toString() {
        return "Road " + id
            + ": " + start.getName()
            + " - " + end.getName()
            + " | Distance: " + distance + " km"
            + " | Blocked: " + blocked;
    }
}