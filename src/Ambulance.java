public class Ambulance extends EmergencyVehicle {

    public Ambulance(int id, String name, Location currentLocation) {
        super(id, name, currentLocation);
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.MEDICAL
            || type == EmergencyType.ACCIDENT;
    }
}