import java.util.ArrayList;

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

        
        Location hospital = new Location(1, "Central Hospital", 150, 150);
        Location junctionA = new Location(2, "Junction A", 350, 150);
        Location junctionB = new Location(3, "Junction B", 350, 350);
        Location station = new Location(4, "Station", 600, 150);
        Location mall = new Location(5, "Shopping Mall", 600, 350);

        city.addLocation(hospital);
        city.addLocation(junctionA);
        city.addLocation(junctionB);
        city.addLocation(station);
        city.addLocation(mall);

        
        city.addRoad(new Road(101, hospital, junctionA, 2.0, 60));
        city.addRoad(new Road(102, junctionA, junctionB, 3.0, 60));
        city.addRoad(new Road(103, junctionA, station, 1.5, 40));
        city.addRoad(new Road(104, junctionB, mall, 2.0, 40));
        city.addRoad(new Road(105, station, mall, 2.0, 40));

        
        Ambulance ambulance = new Ambulance(
            1, "Ambulance A", hospital
        );

        
        DispatchManager manager = new DispatchManager(city);
        manager.addAmbulance(ambulance);

        
        CityMapView map = new CityMapView(city);
        map.setAmbulancePosition(hospital);
        map.hideEmergency();

        PathFinder pathFinder = new PathFinder();

        
        Label heading = new Label(
            "RESQ CITY - Ambulance Simulator"
        );

        heading.setStyle(
            "-fx-font-size: 22px; -fx-font-weight: bold;"
        );

        
        Label status = new Label(
            "System ready. Waiting for emergency..."
        );

        
        ComboBox<Location> locationBox = new ComboBox<>();

        locationBox.getItems().addAll(
            city.getAllLocations()
        );

        locationBox.setValue(mall);

        
        Button dispatchButton = new Button(
            "Dispatch Ambulance"
        );

        Button resetButton = new Button(
            "Reset Ambulance"
        );

        
        dispatchButton.setOnAction(event -> {

            if (dispatching) {
                return;
            }

            Location destination = locationBox.getValue();

            if (destination == null) {
                status.setText("Select an emergency location.");
                return;
            }

            
            Emergency emergency = new Emergency(
                nextEmergencyId++, destination
            );

            
            Ambulance selected = manager.dispatch(emergency);

            if (selected == null) {
                status.setText(
                    "No ambulance available or no route to "
                    + destination.getName()
                );
                return;
            }

            
            ArrayList<Location> route = pathFinder.findFastestPath(
                city,
                selected.getCurrentLocation(),
                destination
            );

            if (route.isEmpty()) {
                selected.setAvailable(true);

                status.setText(
                    "No route to " + destination.getName()
                );

                return;
            }

            
            map.setEmergencyPosition(destination);

            dispatching = true;

            
            dispatchButton.setDisable(true);
            resetButton.setDisable(true);
            locationBox.setDisable(true);

            status.setText(
                selected.getName()
                + " dispatched to "
                + destination.getName()
            );

            
            map.animateRoute(route, () -> {

                selected.setCurrentLocation(destination);
                selected.setAvailable(true);

                emergency.resolve();

                map.hideEmergency();

                dispatching = false;

                
                dispatchButton.setDisable(false);
                resetButton.setDisable(false);
                locationBox.setDisable(false);

                status.setText(
                    "Emergency resolved at "
                    + destination.getName()
                    + "!"
                );
            });
        });

       
        resetButton.setOnAction(event -> {

            if (dispatching) {
                return;
            }

            ambulance.setCurrentLocation(hospital);
            ambulance.setAvailable(true);

            map.setAmbulancePosition(hospital);
            map.hideEmergency();

            status.setText(
                "Ambulance returned to the hospital."
            );
        });

        
        map.setRoadChangeListener(() -> {

            status.setText(
                "Road status updated. Red roads are blocked."
            );
        });

        
        HBox controls = new HBox(
            12,
            new Label("Emergency Location:"),
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
                "Green: Ambulance | Red: Emergency"
            ),
            new Label(
                "Tip: Click any road to block or unblock it."
            )
        );

        dashboard.setPadding(new Insets(20));

        
        BorderPane root = new BorderPane();

        root.setTop(dashboard);
        root.setCenter(map);

        
        Scene scene = new Scene(root, 1050, 780);

        stage.setTitle(
            "RESQ CITY - Ambulance Simulator"
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}