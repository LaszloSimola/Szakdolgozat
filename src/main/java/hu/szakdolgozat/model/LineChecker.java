package hu.szakdolgozat.model;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.util.List;

public class LineChecker {

    // Method to check if lines have something on one end
    public static void checkLines(List<OwnLine> lines) {
        for (OwnLine line : lines) {
            checkLineEnd(line.startXProperty(), line.startYProperty(), "start");
            checkLineEnd(line.endXProperty(), line.endYProperty(), "end");
        }
    }

    // Helper method to check if a line end is bound
    private static void checkLineEnd(DoubleProperty xProperty, DoubleProperty yProperty, String end) {
        if (xProperty.isBound() && yProperty.isBound()) {
            System.out.println("Line " + end + " is bound to something.");
        } else {
            System.out.println("Line " + end + " is not bound.");
        }
    }
    public static boolean isLineEndBoundToEntity(Line line, Node entity) {
        DoubleProperty startXProperty = line.startXProperty();
        DoubleProperty startYProperty = line.startYProperty();
        DoubleProperty endXProperty = line.endXProperty();
        DoubleProperty endYProperty = line.endYProperty();

        Bounds entityBounds = entity.getBoundsInParent();

        // Check if either start or end properties are bound to entity bounds
        boolean isStartBoundToEntity = startXProperty.isBound() && startYProperty.isBound() &&
                startXProperty.get() == entityBounds.getMinX() + entityBounds.getWidth() / 2 &&
                startYProperty.get() == entityBounds.getMinY() + entityBounds.getHeight() / 2;

        boolean isEndBoundToEntity = endXProperty.isBound() && endYProperty.isBound() &&
                endXProperty.get() == entityBounds.getMinX() + entityBounds.getWidth() / 2 &&
                endYProperty.get() == entityBounds.getMinY() + entityBounds.getHeight() / 2;

        // Return true if either end is not bound to the entity
        return isStartBoundToEntity || isEndBoundToEntity;
    }
    public static boolean isBothEndsBound(Line line) {
        DoubleProperty startXProperty = line.startXProperty();
        DoubleProperty startYProperty = line.startYProperty();
        DoubleProperty endXProperty = line.endXProperty();
        DoubleProperty endYProperty = line.endYProperty();

        return startXProperty.isBound() && startYProperty.isBound() &&
                endXProperty.isBound() && endYProperty.isBound();
    }
}
