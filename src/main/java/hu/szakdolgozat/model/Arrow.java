package hu.szakdolgozat.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Arrow extends Group {

    private final Line line;
    private final Line arrow1;
    private final Line arrow2;

    private static final double ARROW_LENGTH = 20;
    private static final double ARROW_WIDTH = 7;

    private final DoubleProperty startX = new SimpleDoubleProperty();
    private final DoubleProperty startY = new SimpleDoubleProperty();
    private final DoubleProperty endX = new SimpleDoubleProperty();
    private final DoubleProperty endY = new SimpleDoubleProperty();

    public Arrow() {
        this.line = new Line();
        this.arrow1 = new Line();
        this.arrow2 = new Line();

        getChildren().addAll(line, arrow1, arrow2);

        // Bind the line to the properties
        line.startXProperty().bind(startX);
        line.startYProperty().bind(startY);
        line.endXProperty().bind(endX);
        line.endYProperty().bind(endY);

        // Add listeners to update arrowheads
        ChangeListener<Number> updateListener = (obs, oldVal, newVal) -> updateArrowheads();
        startX.addListener(updateListener);
        startY.addListener(updateListener);
        endX.addListener(updateListener);
        endY.addListener(updateListener);
    }

    private void updateArrowheads() {
        double ex = endX.get();
        double ey = endY.get();
        double sx = startX.get();
        double sy = startY.get();

        arrow1.setEndX(ex);
        arrow1.setEndY(ey);
        arrow2.setEndX(ex);
        arrow2.setEndY(ey);

        if (ex == sx && ey == sy) {
            arrow1.setStartX(ex);
            arrow1.setStartY(ey);
            arrow2.setStartX(ex);
            arrow2.setStartY(ey);
        } else {
            double factor = ARROW_LENGTH / Math.hypot(sx - ex, sy - ey);
            double factorO = ARROW_WIDTH / Math.hypot(sx - ex, sy - ey);

            // Part in direction of main line
            double dx = (sx - ex) * factor;
            double dy = (sy - ey) * factor;

            // Part perpendicular to main line
            double ox = (sx - ex) * factorO;
            double oy = (sy - ey) * factorO;

            arrow1.setStartX(ex + dx - oy);
            arrow1.setStartY(ey + dy + ox);
            arrow2.setStartX(ex + dx + oy);
            arrow2.setStartY(ey + dy - ox);
        }
    }

    // StartX Property
    public final void setStartX(double value) {
        startX.set(value);
    }

    public final double getStartX() {
        return startX.get();
    }

    public final DoubleProperty startXProperty() {
        return startX;
    }

    // StartY Property
    public final void setStartY(double value) {
        startY.set(value);
    }

    public final double getStartY() {
        return startY.get();
    }

    public final DoubleProperty startYProperty() {
        return startY;
    }

    // EndX Property
    public final void setEndX(double value) {
        endX.set(value);
    }

    public final double getEndX() {
        return endX.get();
    }

    public final DoubleProperty endXProperty() {
        return endX;
    }

    // EndY Property
    public final void setEndY(double value) {
        endY.set(value);
    }

    public final double getEndY() {
        return endY.get();
    }

    public final DoubleProperty endYProperty() {
        return endY;
    }
}
