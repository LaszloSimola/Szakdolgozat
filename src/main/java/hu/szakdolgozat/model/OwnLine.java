package hu.szakdolgozat.model;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.List;

public class OwnLine extends Line implements Selectable {
    private Node startNode;
    private Node endNode;

    public OwnLine() {
        this.setStrokeWidth(1.5);
        this.toBack();
    }

    boolean isSelected = false;

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            this.toBack();
            this.setStroke(Color.RED);
        } else {
            this.setStroke(Color.BLACK);
        }
        isSelected = selected;
    }
    public void unbind() {
        if (startNode != null && endNode != null) {
            startXProperty().unbind();
            startYProperty().unbind();
            endXProperty().unbind();
            endYProperty().unbind();
        }
        // Reset the stored nodes
        startNode = null;
        endNode = null;
    }

}
