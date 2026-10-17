import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CityMapView extends Pane {

    private final City city;

    private final Map<EmergencyVehicle, Circle> vehicleMarkers = new HashMap<>();

    private final Circle ambulance = new Circle(12, Color.LIMEGREEN);
    private final Circle emergencyMarker = new Circle(14, Color.RED);

    private SequentialTransition movement;
    private Runnable roadChangeListener;

    public CityMapView(City city) {
        this.city = city;

        setPrefSize(1000, 650);
        setStyle("-fx-background-color: #142033;");

        drawMap();
    }

    public void setRoadChangeListener(Runnable listener) {
        this.roadChangeListener = listener;
    }

    public void drawMap() {
        getChildren().clear();

        for (Road road : city.getAllRoads()) {

            Location start = road.getStart();
            Location end = road.getEnd();

            Line line = new Line(
                start.getX(),
                start.getY(),
                end.getX(),
                end.getY()
            );

            line.setStroke(
                road.isBlocked() ? Color.RED : Color.GRAY
            );

            line.setStrokeWidth(7);

            line.setOnMouseClicked(event -> {

                if (isMoving()) {
                    return;
                }

                road.setBlocked(!road.isBlocked());

                drawMap();

                if (roadChangeListener != null) {
                    roadChangeListener.run();
                }
            });

            getChildren().add(line);
        }

        for (Location location : city.getAllLocations()) {

            Circle circle = new Circle(
                location.getX(),
                location.getY(),
                12
            );

            circle.setFill(Color.DODGERBLUE);
            circle.setStroke(Color.WHITE);

            Text label = new Text(
                location.getX() + 15,
                location.getY() - 15,
                location.getName()
            );

            label.setFill(Color.WHITE);
            label.setFont(Font.font(14));

            getChildren().addAll(circle, label);
        }

        getChildren().add(emergencyMarker);

        if (vehicleMarkers.isEmpty()) {

            getChildren().add(ambulance);
            ambulance.toFront();

        } else {

            getChildren().addAll(vehicleMarkers.values());

            for (Circle marker : vehicleMarkers.values()) {
                marker.toFront();
            }
        }

        emergencyMarker.toFront();
    }

    public void addVehicle(EmergencyVehicle vehicle) {

        if (vehicle == null || vehicleMarkers.containsKey(vehicle)) {
            return;
        }

        Color color;

        if (vehicle instanceof FireTruck) {
            color = Color.ORANGERED;
        } else if (vehicle instanceof PoliceCar) {
            color = Color.DEEPSKYBLUE;
        } else {
            color = Color.LIMEGREEN;
        }

        Circle marker = new Circle(10, color);
        marker.setStroke(Color.WHITE);

        vehicleMarkers.put(vehicle, marker);

        setVehiclePosition(vehicle, vehicle.getCurrentLocation());

        drawMap();
    }

    public void setVehiclePosition(
            EmergencyVehicle vehicle,
            Location location) {

        Circle marker = vehicleMarkers.get(vehicle);

        if (marker == null || location == null) {
            return;
        }

        marker.setCenterX(location.getX());
        marker.setCenterY(location.getY());

        marker.setTranslateX(0);
        marker.setTranslateY(0);
    }

    public void setAmbulancePosition(Location location) {

        ambulance.setCenterX(location.getX());
        ambulance.setCenterY(location.getY());

        ambulance.setTranslateX(0);
        ambulance.setTranslateY(0);
    }

    public void setEmergencyPosition(Location location) {

        emergencyMarker.setCenterX(location.getX());
        emergencyMarker.setCenterY(location.getY());

        emergencyMarker.setVisible(true);
    }

    public void hideEmergency() {
        emergencyMarker.setVisible(false);
    }

    public boolean isMoving() {
        return movement != null
            && movement.getStatus() == Animation.Status.RUNNING;
    }

    public void animateRoute(
            EmergencyVehicle vehicle,
            List<Location> route,
            Runnable onArrival) {

        Circle marker = vehicleMarkers.get(vehicle);

        if (marker == null || route == null ||
                route.isEmpty() || isMoving()) {
            return;
        }

        setVehiclePosition(vehicle, route.get(0));

        movement = new SequentialTransition();

        for (int i = 1; i < route.size(); i++) {

            Location start = route.get(i - 1);
            Location end = route.get(i);

            double dx = end.getX() - start.getX();
            double dy = end.getY() - start.getY();

            double distance = Math.hypot(dx, dy);

            double seconds = Math.max(
                0.5,
                distance / 120.0
            );

            TranslateTransition transition =
                new TranslateTransition(
                    Duration.seconds(seconds),
                    marker
                );

            transition.setByX(dx);
            transition.setByY(dy);

            transition.setInterpolator(Interpolator.LINEAR);

            movement.getChildren().add(transition);
        }

        movement.setOnFinished(event -> {

            Location destination = route.get(route.size() - 1);

            setVehiclePosition(vehicle, destination);
            vehicle.setCurrentLocation(destination);

            if (onArrival != null) {
                onArrival.run();
            }
        });

        movement.play();
    }

    public void animateRoute(
            List<Location> route,
            Runnable onArrival) {

        if (route == null || route.isEmpty() || isMoving()) {
            return;
        }

        setAmbulancePosition(route.get(0));

        movement = new SequentialTransition();

        for (int i = 1; i < route.size(); i++) {

            Location start = route.get(i - 1);
            Location end = route.get(i);

            double dx = end.getX() - start.getX();
            double dy = end.getY() - start.getY();

            double distance = Math.hypot(dx, dy);

            double seconds = Math.max(
                0.5,
                distance / 120.0
            );

            TranslateTransition transition =
                new TranslateTransition(
                    Duration.seconds(seconds),
                    ambulance
                );

            transition.setByX(dx);
            transition.setByY(dy);

            transition.setInterpolator(Interpolator.LINEAR);

            movement.getChildren().add(transition);
        }

        movement.setOnFinished(event -> {

            Location destination = route.get(route.size() - 1);

            setAmbulancePosition(destination);

            if (onArrival != null) {
                onArrival.run();
            }
        });

        movement.play();
    }
}