public class Main {

    public static void main(String[] args) {

        System.out.println("=== RESQ CITY SIMULATOR ===");

        City city = new City("RESQ CITY");

        Location hospital = new Location(1, "Hospital", 100, 100);
        Location junctionA = new Location(2, "Junction A", 200, 100);
        Location junctionB = new Location(3, "Junction B", 300, 100);
        Location station = new Location(4, "Station", 200, 200);
        Location mall = new Location(5, "Shopping Mall", 300, 200);

        city.addLocation(hospital);
        city.addLocation(junctionA);
        city.addLocation(junctionB);
        city.addLocation(station);
        city.addLocation(mall);

        Road road1 = new Road(101, hospital, junctionA, 2.0, 60);
        Road road2 = new Road(102, junctionA, junctionB, 3.0, 50);
        Road road3 = new Road(103, junctionA, station, 1.5, 40);
        Road road4 = new Road(104, junctionB, mall, 2.0, 60);
        Road road5 = new Road(105, station, mall, 2.5, 50);

        city.addRoad(road1);
        city.addRoad(road2);
        city.addRoad(road3);
        city.addRoad(road4);
        city.addRoad(road5);

        System.out.println("\nLocations:");

        for (Location place : city.getAllLocations()) {
            System.out.println(place);
        }

        System.out.println("\nRoads:");

        for (Road road : city.getAllRoads()) {
            System.out.println(road);
        }

        System.out.println("\nTraffic Test:");

        System.out.println("Normal time: " + road1.calculateTravelTime());

        road1.setTrafficMultiplier(2.0);

        System.out.println("Heavy traffic time: " + road1.calculateTravelTime());

        road1.setTrafficMultiplier(1.0);

        System.out.println("\nFastest Route:");

        PathFinder pathFinder = new PathFinder();

        for (Location place : pathFinder.findFastestPath(city, hospital, mall)) {
            System.out.println(place.getName());
        }

        System.out.println("\nEmergency Dispatch:");

        Ambulance ambulance = new Ambulance(1, "Ambulance 1", hospital);

        DispatchManager manager = new DispatchManager(city);
        manager.addAmbulance(ambulance);

        Emergency emergency = new Emergency(1, mall);

        Ambulance selected = manager.dispatch(emergency);

        if (selected != null) {
            System.out.println("Selected: " + selected.getName());

            SimulationEngine engine = new SimulationEngine(city);
            engine.simulateAmbulance(selected, emergency);
        }

        System.out.println("\nSimulation completed!");
    }
}