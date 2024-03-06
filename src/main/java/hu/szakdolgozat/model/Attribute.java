package hu.szakdolgozat.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Attribute extends Ellipse {
    public Attribute() {
    }

    public Attribute(double x, double y){
        super(x,y);
        setFill(Color.TRANSPARENT); // Set fill color to transparent
        setStroke(Color.BLACK);
    }
}
