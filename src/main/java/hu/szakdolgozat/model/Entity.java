package hu.szakdolgozat.model;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Entity extends Rectangle {

    private double mouseX;
    private double mouseY;

    public Entity() {

    }

    public Entity(double x, double y, double width, double height) {
        super(x, y, width, height);
        setFill(Color.TRANSPARENT); // Set fill color to transparent
        setStroke(Color.BLACK);

        // Add event handlers for dragging
        setOnMousePressed(this::onMousePressed);
        setOnMouseDragged(this::onMouseDragged);
    }

    private void onMousePressed(MouseEvent event) {
        mouseX = event.getSceneX() - getTranslateX();
        mouseY = event.getSceneY() - getTranslateY();
    }

    private void onMouseDragged(MouseEvent event) {
        double newX = event.getSceneX() - mouseX;
        double newY = event.getSceneY() - mouseY;
        setTranslateX(newX);
        setTranslateY(newY);
    }
}
