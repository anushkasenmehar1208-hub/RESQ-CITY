public class Location {

    private final int id;
    private final String name;
    private final double x;
    private final double y;

    public Location(int id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y; 
    } 

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return name + " (" + x + ", " + y + ")";
    }
} 