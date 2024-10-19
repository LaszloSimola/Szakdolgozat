package hu.szakdolgozat.model;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
public class Relation extends StackPane implements Serializable, Selectable, Draggable {
    private double posX;
    private double posY;
    private Color strokeColor = Color.BLACK;
    private double strokeWidth = 1.0;
    private double size = 1.0; // Example initial scale factor

    boolean isSelected = false;
    private Polygon polygon;
    private Polygon outerPolygon; // Outer polygon for the second outline
    private Text textNode;
    private boolean IsIdentify = false;

    public Relation(@JsonProperty("posX") double posX, @JsonProperty("posY") double posY, @JsonProperty("points") double... points) {
        this.posX = posX;
        this.posY = posY;

        polygon = new Polygon(points);
        polygon.setFill(Color.WHITE);
        polygon.setStroke(strokeColor);
        polygon.setStrokeWidth(strokeWidth);

        // Create the outer polygon slightly larger
        outerPolygon = new Polygon(scalePoints(points, 15)); // Add 10 units to create a larger outline
        outerPolygon.setFill(Color.TRANSPARENT);
        outerPolygon.setStroke(strokeColor);
        outerPolygon.setStrokeWidth(2);
        outerPolygon.setVisible(IsIdentify); // Initially hidden

        // Add both polygons to the StackPane, outer first so it is behind
        getChildren().addAll(outerPolygon, polygon);
        setTextNode("Relation");

        setAlignment(polygon, Pos.CENTER);
        setLayoutX(posX);
        setLayoutY(posY);

        // Ensure that text is always centered
        widthProperty().addListener((obs, oldVal, newVal) -> setAlignment(textNode, Pos.CENTER));
        heightProperty().addListener((obs, oldVal, newVal) -> setAlignment(textNode, Pos.CENTER));
    }

    public boolean isISIdentify() {
        return IsIdentify;
    }

    public void setIsIdentify(boolean IsIdentify) {
        this.IsIdentify = IsIdentify;
        outerPolygon.setVisible(IsIdentify);

    }

    public Text getTextNode() {
        return textNode;
    }

    public void setTextNode(String textContent) {
        if (textNode == null) {
            textNode = new Text();
            textNode.setFont(Font.font(12));
            textNode.setFill(Color.BLACK);
            textNode.setTextOrigin(VPos.CENTER);
            textNode.setTextAlignment(TextAlignment.CENTER);
            getChildren().add(textNode);
        }
        textNode.setText(textContent);
        setAlignment(textNode, Pos.CENTER); // Center the text within the StackPane
    }

    public Relation(double... points) {
        this(0, 0, points);
    }

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
        polygon.setStroke(strokeColor);
        outerPolygon.setStroke(strokeColor); // Update the outer polygon stroke
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        polygon.setStrokeWidth(strokeWidth);
        outerPolygon.setStrokeWidth(strokeWidth);
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
        polygon.setScaleX(size); // Adjust scaleX
        polygon.setScaleY(size); // Adjust scaleY
        outerPolygon.setScaleX(size); // Scale outer polygon
        outerPolygon.setScaleY(size); // Scale outer polygon
    }

    @Override
    public void drag(double deltaX, double deltaY) {
        setPosX(getLayoutX() + deltaX);
        setPosY(getLayoutY() + deltaY);
        System.out.println("Dragged to posX: " + posX + ", posY: " + posY);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            polygon.setStroke(Color.RED);
            outerPolygon.setStroke(Color.RED);

        } else {
            polygon.setStroke(strokeColor);
            outerPolygon.setStroke(strokeColor);

        }
        isSelected = selected;
    }

    // Scale points for the outer polygon, relative to the center of the polygon
    private double[] scalePoints(double[] points, double scaleOffset) {
        double[] scaledPoints = new double[points.length];

        // Calculate the centroid (center) of the polygon
        double centerX = 0;
        double centerY = 0;
        for (int i = 0; i < points.length; i += 2) {
            centerX += points[i];
            centerY += points[i + 1];
        }
        centerX /= ((double) points.length / 2);
        centerY /= ((double) points.length / 2);

        // Scale each point relative to the centroid
        for (int i = 0; i < points.length; i += 2) {
            double deltaX = points[i] - centerX;
            double deltaY = points[i + 1] - centerY;

            // Apply scaling
            scaledPoints[i] = centerX + deltaX * (1 + scaleOffset / 100); // Scale X
            scaledPoints[i + 1] = centerY + deltaY * (1 + scaleOffset / 100); // Scale Y
        }

        return scaledPoints;
    }


    @Override
    public boolean contains(double x, double y) {
        System.out.println(polygon.contains(x,y));
        return getBoundsInParent().contains(x,y);
    }
    // Convert to a DTO for serialization
    public RelationDTO toDTO() {
        return new RelationDTO(posX, posY, polygon.getPoints().stream().mapToDouble(Double::doubleValue).toArray(), textNode.getText(), strokeColor.toString(), strokeWidth, size);
    }

    // Convert from a DTO to a Relation
    public static Relation fromDTO(RelationDTO dto) {
        Relation relation = new Relation(dto.getPosX(), dto.getPosY(), dto.getPoints());
        relation.setTextNode(dto.getText());
        relation.setStrokeColor(Color.valueOf(dto.getStrokeColor()));
        relation.setStrokeWidth(dto.getStrokeWidth());
        relation.setSize(dto.getSize());
        return relation;
    }

    // DTO class for Relation
    public static class RelationDTO {
        private double posX;
        private double posY;
        private double[] points;
        private String text;
        private String strokeColor;
        private double strokeWidth;
        private double size;

        public RelationDTO(@JsonProperty("posX") double posX, @JsonProperty("posY") double posY, @JsonProperty("points") double[] points, @JsonProperty("text") String text, @JsonProperty("strokeColor") String strokeColor, @JsonProperty("strokeWidth") double strokeWidth, @JsonProperty("size") double size) {
            this.posX = posX;
            this.posY = posY;
            this.points = points;
            this.text = text;
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
            this.size = size;
        }

        public double getPosX() {
            return posX;
        }

        public double getPosY() {
            return posY;
        }

        public double[] getPoints() {
            return points;
        }

        public String getText() {
            return text;
        }

        public String getStrokeColor() {
            return strokeColor;
        }

        public double getStrokeWidth() {
            return strokeWidth;
        }

        public double getSize() {
            return size;
        }
    }
}
