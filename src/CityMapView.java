import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CityMapView extends Pane {

    private City city;
    private Circle ambulance = new Circle(10, Color.LIMEGREEN);
    private Circle emergency = new Circle(12, Color.RED);
    private SequentialTransition movement;
    private Runnable roadChangeListener;

    public CityMapView(City city) {
        this.city = city;
        setPrefSize(1000, 650);
        setStyle("-fx-background-color: #142033;");
        emergency.setVisible(false);
        drawMap();
    }

    public void setRoadChangeListener(Runnable listener) {
        roadChangeListener = listener;
    }

    public void drawMap() {
        getChildren().clear();

        for (Road road : city.getAllRoads()) {
            Location start = road.getStart();
            Location end = road.getEnd();

            Line line = new Line(start.getX(), start.getY(), end.getX(), end.getY());
            line.setStroke(road.isBlocked() ? Color.RED : Color.GRAY);
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

            String traffic = "Low";

            if (road.getTrafficMultiplier() >= 2.0) {
                traffic = "Heavy";
            } else if (road.getTrafficMultiplier() >= 1.5) {
                traffic = "Medium";
            }

            String info;

            if (road.isBlocked()) {
                info = road.getDistance() + " km | BLOCKED";
            } else {
                info = road.getDistance() + " km | " + traffic + " | "
                    + Math.round(road.calculateTravelTime()) + " s";
            }

            Label label = new Label(info);
            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-background-color: #25354c; -fx-padding: 3;");
            label.setMouseTransparent(true);

            double x = (start.getX() + end.getX()) / 2;
            double y = (start.getY() + end.getY()) / 2 - 20;

            if (road.getId() == 101) {
                x = x - 25;
                y = y + 42;
            }

            label.setLayoutX(x);
            label.setLayoutY(y);

            getChildren().add(label);
        }

        for (Location place : city.getAllLocations()) {
            Circle circle = new Circle(place.getX(), place.getY(), 12, Color.DODGERBLUE);
            circle.setStroke(Color.WHITE);

            Text name = new Text(
                place.getX() + 15,
                place.getY() - 15,
                place.getName()
            );

            name.setFill(Color.WHITE);
            getChildren().addAll(circle, name);
        }

        getChildren().addAll(ambulance, emergency);
        ambulance.toFront();
        emergency.toFront();
    }

    public void setAmbulancePosition(Location place) {
        ambulance.setCenterX(place.getX());
        ambulance.setCenterY(place.getY());
        ambulance.setTranslateX(0);
        ambulance.setTranslateY(0);
    }

    public void setEmergencyPosition(Location place) {
        emergency.setCenterX(place.getX());
        emergency.setCenterY(place.getY());
        emergency.setVisible(true);
    }

    public void hideEmergency() {
        emergency.setVisible(false);
    }

    public boolean isMoving() {
        return movement != null
            && movement.getStatus() == Animation.Status.RUNNING;
    }

    public void animateRoute(ArrayList<Location> route, Runnable onArrival) {
        if (route == null || route.isEmpty() || isMoving()) {
            return;
        }

        setAmbulancePosition(route.get(0));
        movement = new SequentialTransition();

        for (int i = 1; i < route.size(); i++) {
            Location start = route.get(i - 1);
            Location end = route.get(i);

            double x = end.getX() - start.getX();
            double y = end.getY() - start.getY();
            double seconds = Math.max(0.5, Math.hypot(x, y) / 120);

            TranslateTransition step = new TranslateTransition(
                Duration.seconds(seconds),
                ambulance
            );

            step.setByX(x);
            step.setByY(y);
            movement.getChildren().add(step);
        }

        movement.setOnFinished(event -> {
            setAmbulancePosition(route.get(route.size() - 1));

            if (onArrival != null) {
                onArrival.run();
            }
        });

        movement.play();
    }
}