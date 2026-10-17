public class PoliceCar extends EmergencyVehicle {

    public PoliceCar(int id, String name, Location location) {
        super(id, name, location);
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.CRIME;
    }
}