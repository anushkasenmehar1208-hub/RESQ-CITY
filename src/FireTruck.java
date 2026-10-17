public class FireTruck extends EmergencyVehicle {

    public FireTruck(int id, String name, Location location) {
        super(id, name, location);
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.FIRE;
    }
}