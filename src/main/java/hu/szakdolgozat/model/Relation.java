package hu.szakdolgozat.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Relation extends Polygon {

    public Relation() {
    }

    public Relation(double... doubles) {
        super(doubles);
        setFill(Color.TRANSPARENT);
        setStroke(Color.BLACK);
    }
}
