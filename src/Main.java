public class Main {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       RESQ CITY SIMULATOR      ");
        System.out.println("================================");

        City city = new City("RESQ CITY");

        Location hospital = new Location(1, "Central Hospital", 100, 100);
        Location junctionA = new Location(2, "Junction A", 200, 100);
        Location junctionB = new Location(3, "Junction B", 300, 100);
        Location fireStation = new Location(4, "Fire Station", 200, 200);
        Location shoppingMall = new Location(5, "Shopping Mall", 300, 200);

        city.addLocation(hospital);
        city.addLocation(junctionA);
        city.addLocation(junctionB);
        city.addLocation(fireStation);
        city.addLocation(shoppingMall);

        Road road1 = new Road(101, hospital, junctionA, 2.0, 60);
        Road road2 = new Road(102, junctionA, junctionB, 3.0, 50);
        Road road3 = new Road(103, junctionA, fireStation, 1.5, 40);
        Road road4 = new Road(104, junctionB, shoppingMall, 2.0, 60);
        Road road5 = new Road(105, fireStation, shoppingMall, 2.5, 50);

        city.addRoad(road1);
        city.addRoad(road2);
        city.addRoad(road3);
        city.addRoad(road4);
        city.addRoad(road5);

        System.out.println("\n" + city);

        System.out.println("\n--- LOCATIONS ---");

        for (Location location : city.getAllLocations()) {
            System.out.println(location);
        }

        System.out.println("\n--- ROADS ---");

        for (Road road : city.getAllRoads()) {
            System.out.println(road);
        }

        System.out.println("\n--- ROADS CONNECTED TO JUNCTION A ---");

        for (Road road : city.getConnectedRoads(2)) {
            System.out.println(road);
        }

        System.out.println("\n--- TRAFFIC SIMULATION ---");

        System.out.println("Normal travel time: "
                + road1.calculateTravelTime() + " seconds");

        road1.setTrafficMultiplier(2.0);

        System.out.println("Heavy traffic travel time: "
                + road1.calculateTravelTime() + " seconds");

        road1.setBlocked(true);

        System.out.println("Road blocked: " + road1.isBlocked());

        System.out.println("Travel time after closure: "
                + road1.calculateTravelTime());

        road1.setBlocked(false);
        road1.setTrafficMultiplier(1.0);

        System.out.println("\nRoad reopened successfully!");

        System.out.println("\n--- FASTEST ROUTE TEST ---");

        PathFinder pathFinder = new PathFinder();

        System.out.println("Hospital to Shopping Mall:");

        for (Location location : pathFinder.findFastestPath(
                city, hospital, shoppingMall)) {
            System.out.println(location.getName());
        }

        System.out.println("\n--- EMERGENCY DISPATCH TEST ---");

        DispatchManager dispatchManager = new DispatchManager(city);

        
        Ambulance ambulance1 = new Ambulance(
                1, "Ambulance A", hospital
        );

        FireTruck fireTruck1 = new FireTruck(
                2, "Fire Truck A", fireStation
        );

        PoliceCar policeCar1 = new PoliceCar(
                3, "Police Car A", junctionB
        );

        dispatchManager.addVehicle(ambulance1);
        dispatchManager.addVehicle(fireTruck1);
        dispatchManager.addVehicle(policeCar1);

        
        Emergency emergency = new Emergency(
                1,
                EmergencyType.MEDICAL,
                shoppingMall,
                4
        );

        
        EmergencyVehicle selected = dispatchManager.dispatch(emergency);

        if (selected != null) {

            System.out.println(
                    "Selected vehicle: " + selected.getName()
            );

            System.out.println(
                    "Vehicle available: " + selected.isAvailable()
            );
        }

        
        if (selected instanceof Ambulance ambulance) {

            SimulationEngine engine = new SimulationEngine(city);

            engine.simulateAmbulance(ambulance, emergency);
        }

        System.out.println("\nRESQ CITY initialization completed!");
    }
}