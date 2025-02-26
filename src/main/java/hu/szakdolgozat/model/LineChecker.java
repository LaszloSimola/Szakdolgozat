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

        double arrowStartX = arrow.startXProperty().get();
        double arrowStartY = arrow.startYProperty().get();
        double arrowEndX = arrow.endXProperty().get();
        double arrowEndY = arrow.endYProperty().get();

        // Check if the start or end of the arrow is within the entity's bounds
        boolean isStartBound = entityBounds.contains(arrowStartX, arrowStartY);
        boolean isEndBound = entityBounds.contains(arrowEndX, arrowEndY);

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
