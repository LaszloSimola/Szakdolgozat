package hu.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.io.Serializable;

public class SpecializerRelation extends StackPane implements Serializable, Selectable, Draggable {
    private double posX;
    private double posY;
    private Color strokeColor = Color.BLACK;
    private double strokeWidth = 1.0;
    boolean isSelected = false;
    private Polygon triangle;


    public SpecializerRelation() {
        this(0, 0, 65, 65); // Default values for position, width, and height
    }

    public SpecializerRelation(double posX, double posY) {
        this(posX, posY, 65, 65); // Default values for width and height
    }

    public SpecializerRelation(@JsonProperty("x") double posX, @JsonProperty("y") double posY, @JsonProperty("width") double width, @JsonProperty("height") double height) {
        this.posX = posX;
        this.posY = posY;

        triangle = createTriangle(width, height);
        triangle.setFill(Color.WHITE);
        triangle.setStroke(strokeColor);
        triangle.setStrokeWidth(strokeWidth);

        // Add the triangle to the StackPane
        getChildren().add(triangle);


        setAlignment(triangle, Pos.CENTER); // Center the triangle within the StackPane
        setLayoutX(posX);
        setLayoutY(posY);

    }

    private Polygon createTriangle(double width, double height) {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                width / 2, 0.0,        // Top middle vertex (previously bottom middle)
                width, height,         // Bottom right vertex (previously top right)
                0.0, height            // Bottom left vertex (previously top left)
        );
        return polygon;
    }


    // Getters, Setters
    public double getPosX() {
        return posX;
    }

    public void setPosX(double x) {
        posX = x;
        setLayoutX(x);
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double y) {
        posY = y;
        setLayoutY(y);
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
        triangle.setStroke(strokeColor);
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        triangle.setStrokeWidth(strokeWidth);
    }

    @Override
    public void drag(double deltaX, double deltaY) {
        setPosX(getPosX() + deltaX);
        setPosY(getPosY() + deltaY);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            triangle.setStroke(Color.RED);
        } else {
            triangle.setStroke(strokeColor);
        }
        isSelected = selected;
    }

    public Polygon getTriangle() {
        return triangle;
    }

    public void updateSize(double width, double height) {
        triangle.getPoints().setAll(
                0.0, 0.0,            // Top vertex
                width, 0.0,          // Bottom right vertex
                width / 2, height    // Bottom middle vertex
        );
    }

    @Override
    public boolean contains(double x, double y) {
        return triangle.contains(x - getLayoutX(), y - getLayoutY());
    }

    // Convert to a DTO for serialization
    public SpecializerDTO toDTO() {
        return new SpecializerDTO(posX, posY, triangle.getPoints().get(2), triangle.getPoints().get(4), strokeColor.toString(), strokeWidth);
    }

    // Convert from a DTO to a Specializer
    public static SpecializerRelation fromDTO(SpecializerDTO dto) {
        SpecializerRelation specializer = new SpecializerRelation(dto.getPosX(), dto.getPosY(), dto.getWidth(), dto.getHeight());
        specializer.setStrokeColor(Color.valueOf(dto.getStrokeColor()));
        specializer.setStrokeWidth(dto.getStrokeWidth());
        return specializer;
    }

    // DTO class for Specializer
    public static class SpecializerDTO {
        private double posX;
        private double posY;
        private double width;
        private double height;
        private String strokeColor;
        private double strokeWidth;

        public SpecializerDTO(@JsonProperty("posX") double posX, @JsonProperty("posY") double posY, @JsonProperty("width") double width, @JsonProperty("height") double height, @JsonProperty("strokeColor") String strokeColor, @JsonProperty("strokeWidth") double strokeWidth) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
        }

        // Getters
        public double getPosX() {
            return posX;
        }

        public double getPosY() {
            return posY;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }



        public String getStrokeColor() {
            return strokeColor;
        }

        public double getStrokeWidth() {
            return strokeWidth;
        }
    }
}
