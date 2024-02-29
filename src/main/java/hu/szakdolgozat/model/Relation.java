package hu.szakdolgozat.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Relation extends Polygon {
    private double x;
    private double y;

    public Relation() {
    }

    public Relation(double... doubles) {
        super(doubles);
        setFill(Color.TRANSPARENT); // Set fill color to transparent
        setStroke(Color.BLACK);
    }
}
