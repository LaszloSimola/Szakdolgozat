package hu.szakdolgozat.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Entity extends Rectangle {
    private double x;
    private double y;
    private double width;
    private double height;

    public Entity() {
    }

    public Entity(double x, double y, double width, double height) {
        super(x,y,width,height);
        setFill(Color.TRANSPARENT); // Set fill color to transparent
        setStroke(Color.BLACK);
    }

}
