package hu.szakdolgozat.model;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import java.util.List;

public class LineChecker {

    // Method to check if arrows have something on one end
    public static void checkArrows(List<Arrow> arrows) {
        for (Arrow arrow : arrows) {
            checkArrowEnd(arrow.startXProperty(), arrow.startYProperty(), "start");
            checkArrowEnd(arrow.endXProperty(), arrow.endYProperty(), "end");
        }
    }

    // Helper method to check if an arrow end is bound
    private static void checkArrowEnd(DoubleProperty xProperty, DoubleProperty yProperty, String end) {
        if (xProperty.isBound() && yProperty.isBound()) {
            System.out.println("Arrow " + end + " is bound to something.");
        } else {
            System.out.println("Arrow " + end + " is not bound.");
        }
    }

    public static boolean isArrowEndBoundToEntity(Arrow arrow, Node entity) {
        Bounds entityBounds = entity.getBoundsInParent();

        double entityCenterX = entityBounds.getMinX() + entityBounds.getWidth() / 2;
        double entityCenterY = entityBounds.getMinY() + entityBounds.getHeight() / 2;

        // Approximate bound detection (with a small tolerance for floating-point inaccuracies)
        double tolerance = 0.5; // Adjust if necessary

        boolean isStartBound = arrow.startXProperty().isBound() && arrow.startYProperty().isBound() &&
                Math.abs(arrow.startXProperty().get() - entityCenterX) < tolerance &&
                Math.abs(arrow.startYProperty().get() - entityCenterY) < tolerance;

        boolean isEndBound = arrow.endXProperty().isBound() && arrow.endYProperty().isBound() &&
                Math.abs(arrow.endXProperty().get() - entityCenterX) < tolerance &&
                Math.abs(arrow.endYProperty().get() - entityCenterY) < tolerance;

        return isStartBound || isEndBound;
    }


    // Checks if both ends of an arrow are bound
    public static boolean isBothEndsBound(Arrow arrow) {
        DoubleProperty startXProperty = arrow.startXProperty();
        DoubleProperty startYProperty = arrow.startYProperty();
        DoubleProperty endXProperty = arrow.endXProperty();
        DoubleProperty endYProperty = arrow.endYProperty();

        return startXProperty.isBound() && startYProperty.isBound() &&
                endXProperty.isBound() && endYProperty.isBound();
    }
}
