package hu.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Arrow extends Group implements Draggable {

    private final Line line;
    private final Line arrow1;
    private final Line arrow2;
    private final Line arrow3;
    private final Line arrow4;

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
        this.arrow3 = new Line();
        this.arrow4 = new Line();

        getChildren().addAll(line, arrow1, arrow2, arrow3, arrow4);

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

    public void setArrowAtStart(boolean visible) {
        arrow3.setVisible(visible);
        arrow4.setVisible(visible);
    }

    public void setArrowAtEnd(boolean visible) {
        arrow1.setVisible(visible);
        arrow2.setVisible(visible);
    }

    private void updateArrowheads() {
        double ex = endX.get();
        double ey = endY.get();
        double sx = startX.get();
        double sy = startY.get();

        // Update end arrow
        arrow1.setEndX(ex);
        arrow1.setEndY(ey);
        arrow2.setEndX(ex);
        arrow2.setEndY(ey);

        // Update start arrow
        arrow3.setEndX(sx);
        arrow3.setEndY(sy);
        arrow4.setEndX(sx);
        arrow4.setEndY(sy);

        if (ex == sx && ey == sy) {
            arrow1.setStartX(ex);
            arrow1.setStartY(ey);
            arrow2.setStartX(ex);
            arrow2.setStartY(ey);
            arrow3.setStartX(sx);
            arrow3.setStartY(sy);
            arrow4.setStartX(sx);
            arrow4.setStartY(sy);
        } else {
            double factor = ARROW_LENGTH / Math.hypot(sx - ex, sy - ey);
            double factorO = ARROW_WIDTH / Math.hypot(sx - ex, sy - ey);

            double dx = (sx - ex) * factor;
            double dy = (sy - ey) * factor;

            double ox = (sx - ex) * factorO;
            double oy = (sy - ey) * factorO;

            arrow1.setStartX(ex + dx - oy);
            arrow1.setStartY(ey + dy + ox);
            arrow2.setStartX(ex + dx + oy);
            arrow2.setStartY(ey + dy - ox);

            arrow3.setStartX(sx - dx - oy);
            arrow3.setStartY(sy - dy + ox);
            arrow4.setStartX(sx - dx + oy);
            arrow4.setStartY(sy - dy - ox);
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

    @Override
    public double getPosX() {
        return 0;
    }

    @Override
    public void setPosX(double x) {

    }

    @Override
    public double getPosY() {
        return 0;
    }

    @Override
    public void setPosY(double y) {

    }

    @Override
    public void drag(double deltaX, double deltaY) {

    }

    // DTO class for Arrow
    public static class ArrowDTO {

        private double startX;
        private double startY;
        private double endX;
        private double endY;
        private boolean isArrowAtStartVisible; // Visibility of start arrows
        private boolean isArrowAtEndVisible;   // Visibility of end arrows

        public ArrowDTO(@JsonProperty("startX") double startX,
                        @JsonProperty("startY") double startY,
                        @JsonProperty("endX") double endX,
                        @JsonProperty("endY") double endY,
                        @JsonProperty("isArrowAtStartVisible") boolean isArrowAtStartVisible,
                        @JsonProperty("isArrowAtEndVisible") boolean isArrowAtEndVisible) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.isArrowAtStartVisible = isArrowAtStartVisible;
            this.isArrowAtEndVisible = isArrowAtEndVisible;
        }

        public double getStartX() {
            return startX;
        }

        public void setStartX(double startX) {
            this.startX = startX;
        }

        public double getStartY() {
            return startY;
        }

        public void setStartY(double startY) {
            this.startY = startY;
        }

        public double getEndX() {
            return endX;
        }

        public void setEndX(double endX) {
            this.endX = endX;
        }

        public double getEndY() {
            return endY;
        }

        public void setEndY(double endY) {
            this.endY = endY;
        }

        public boolean isArrowAtStartVisible() {
            return isArrowAtStartVisible;
        }

        public void setArrowAtStartVisible(boolean visible) {
            isArrowAtStartVisible = visible;
        }

        public boolean isArrowAtEndVisible() {
            return isArrowAtEndVisible;
        }

        public void setArrowAtEndVisible(boolean visible) {
            isArrowAtEndVisible = visible;
        }
    }

    // Convert Arrow to DTO
    public ArrowDTO toDTO() {
        return new ArrowDTO(
                getStartX(),
                getStartY(),
                getEndX(),
                getEndY(),
                arrow3.isVisible(), // Check visibility of arrows at the start
                arrow1.isVisible()  // Check visibility of arrows at the end
        );
    }

    public static Arrow fromDTO(ArrowDTO dto) {
        Arrow arrow = new Arrow();
        arrow.setStartX(dto.getStartX());
        arrow.setStartY(dto.getStartY());
        arrow.setEndX(dto.getEndX());
        arrow.setEndY(dto.getEndY());

        // Restore visibility of arrows based on DTO values
        arrow.setArrowAtStart(dto.isArrowAtStartVisible());
        arrow.setArrowAtEnd(dto.isArrowAtEndVisible());

        return arrow;
    }

}


