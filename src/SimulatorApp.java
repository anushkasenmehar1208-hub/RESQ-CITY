import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimulatorApp extends Application {

    private int nextEmergencyId = 1;
    private boolean dispatching = false;

    @Override
    public void start(Stage stage) {

        City city = new City("RESQ CITY");

        Location hospital = new Location(
            1, "Central Hospital", 150, 150
        );

        Location junctionA = new Location(
            2, "Junction A", 350, 150
        );

        Location junctionB = new Location(
            3, "Junction B", 350, 350
        );

        Location fireStation = new Location(
            4, "Fire Station", 600, 150
        );

        Location mall = new Location(
            5, "Shopping Mall", 600, 350
        );

        city.addLocation(hospital);
        city.addLocation(junctionA);
        city.addLocation(junctionB);
        city.addLocation(fireStation);
        city.addLocation(mall);

        city.addRoad(new Road(
            101, hospital, junctionA, 2.0, 60
        ));

        city.addRoad(new Road(
            102, junctionA, junctionB, 3.0, 60
        ));

        city.addRoad(new Road(
            103, junctionA, fireStation, 1.5, 40
        ));

        city.addRoad(new Road(
            104, junctionB, mall, 2.0, 40
        ));

        city.addRoad(new Road(
            105, fireStation, mall, 2.0, 40
        ));

        Ambulance ambulance = new Ambulance(
            1, "Ambulance A", hospital
        );

        FireTruck fireTruck = new FireTruck(
            2, "Fire Truck A", fireStation
        );

        PoliceCar policeCar = new PoliceCar(
            3, "Police Car A", junctionB
        );

        DispatchManager dispatchManager =
            new DispatchManager(city);

        dispatchManager.addVehicle(ambulance);
        dispatchManager.addVehicle(fireTruck);
        dispatchManager.addVehicle(policeCar);

        CityMapView map = new CityMapView(city);

        map.addVehicle(ambulance);
        map.addVehicle(fireTruck);
        map.addVehicle(policeCar);

        map.hideEmergency();

        PathFinder pathFinder = new PathFinder();

        Label heading = new Label(
            "RESQ CITY - Emergency Response Simulator"
        );

        heading.setStyle(
            "-fx-font-size: 22px;"
            + "-fx-font-weight: bold;"
        );

        Label status = new Label(
            "System ready. Waiting for emergency..."
        );

        ComboBox<EmergencyType> typeBox =
            new ComboBox<>();

        typeBox.getItems().addAll(
            EmergencyType.values()
        );

        typeBox.setValue(EmergencyType.MEDICAL);

        ComboBox<Location> locationBox =
            new ComboBox<>();

        locationBox.getItems().addAll(
            city.getAllLocations()
        );

        locationBox.setValue(mall);

        Button dispatchButton = new Button(
            "Dispatch Vehicle"
        );

        Button resetButton = new Button(
            "Reset Vehicles"
        );

        dispatchButton.setOnAction(event -> {

            if (dispatching) {
                return;
            }

            EmergencyType selectedType = typeBox.getValue();
            Location destination = locationBox.getValue();

            if (destination == null || selectedType == null) {
                status.setText(
                    "Select an emergency type and location."
                );
                return;
            }

            Emergency emergency = new Emergency(
                nextEmergencyId++,
                selectedType,
                destination,
                3
            );

            EmergencyVehicle selected =
                dispatchManager.dispatch(emergency);

            if (selected == null) {

                status.setText(
                    "No available compatible vehicle can reach "
                    + destination.getName()
                );

                return;
            }

            List<Location> route =
                pathFinder.findFastestPath(
                    city,
                    selected.getCurrentLocation(),
                    destination
                );

            if (route.isEmpty()) {

                selected.setAvailable(true);

                status.setText(
                    "No available route to "
                    + destination.getName()
                );

                return;
            }

            map.setEmergencyPosition(destination);

            dispatching = true;

            dispatchButton.setDisable(true);
            resetButton.setDisable(true);

            typeBox.setDisable(true);
            locationBox.setDisable(true);

            status.setText(
                selected.getName()
                + " dispatched to "
                + destination.getName()
                + " for "
                + selectedType
            );

            map.animateRoute(selected, route, () -> {

                selected.setCurrentLocation(destination);
                selected.setAvailable(true);

                emergency.resolve();

                map.hideEmergency();

                dispatching = false;

                dispatchButton.setDisable(false);
                resetButton.setDisable(false);

                typeBox.setDisable(false);
                locationBox.setDisable(false);

                status.setText(
                    selected.getName()
                    + " resolved "
                    + selectedType
                    + " at "
                    + destination.getName()
                    + ". Vehicle available again!"
                );
            });
        });

        resetButton.setOnAction(event -> {

            if (dispatching) {
                return;
            }

            ambulance.setCurrentLocation(hospital);
            fireTruck.setCurrentLocation(fireStation);
            policeCar.setCurrentLocation(junctionB);

            ambulance.setAvailable(true);
            fireTruck.setAvailable(true);
            policeCar.setAvailable(true);

            map.setVehiclePosition(ambulance, hospital);
            map.setVehiclePosition(fireTruck, fireStation);
            map.setVehiclePosition(policeCar, junctionB);

            map.hideEmergency();

            status.setText(
                "All vehicles returned to their starting locations."
            );
        });

        map.setRoadChangeListener(() -> {

            status.setText(
                "Road status updated. "
                + "Red roads are blocked."
            );
        });

        HBox controls = new HBox(
            12,
            new Label("Emergency:"),
            typeBox,
            new Label("Location:"),
            locationBox,
            dispatchButton,
            resetButton
        );

        VBox dashboard = new VBox(
            12,
            heading,
            controls,
            status,
            new Label(
                "Green: Ambulance | Orange: Fire Truck | Blue: Police Car"
            ),
            new Label(
                "Tip: Click any road to block or unblock it."
            )
        );

        dashboard.setPadding(new Insets(20));

        BorderPane root = new BorderPane();

        root.setTop(dashboard);
        root.setCenter(map);

        Scene scene = new Scene(
            root,
            1050,
            780
        );

        stage.setTitle(
            "RESQ CITY - Emergency Response Simulator"
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}