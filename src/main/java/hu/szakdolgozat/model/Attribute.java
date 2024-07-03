package hu.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.Serializable;

public class Attribute extends StackPane implements Serializable, Selectable, Draggable {
    private double posX;
    private double posY;
    private Color strokeColor = Color.BLACK;
    private double strokeWidth = 1.0;
    boolean isSelected = false;
    private Ellipse ellipse;
    private Text textNode;

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

    public Attribute() {
        this(0, 0, 30, 50);
    }

    public Attribute(double posX, double posY) {
        this(posX, posY, 30, 50);
    }

    public Attribute(@JsonProperty("x") double posX, @JsonProperty("y") double posY, @JsonProperty("height") double height, @JsonProperty("width") double width) {
        this.posX = posX;
        this.posY = posY;

        ellipse = new Ellipse(width / 2, height / 2);
        ellipse.setFill(Color.WHITE);
        ellipse.setStroke(strokeColor);
        ellipse.setStrokeWidth(strokeWidth);

        getChildren().add(ellipse);
        setTextNode("Attribute");

        setAlignment(Pos.CENTER);
        setLayoutX(posX);
        setLayoutY(posY);

        widthProperty().addListener((obs, oldVal, newVal) -> setAlignment(Pos.CENTER));
        heightProperty().addListener((obs, oldVal, newVal) -> setAlignment(Pos.CENTER));
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
        ellipse.setStroke(strokeColor);
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        ellipse.setStrokeWidth(strokeWidth);
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
            ellipse.setStroke(Color.RED);
        } else {
            ellipse.setStroke(strokeColor);
        }
        isSelected = selected;
    }

    public Ellipse getEllipse() {
        return ellipse;
    }

    @Override
    public boolean contains(double x, double y) {
        double localX = x - (getLayoutX() + getWidth() / 2);
        double localY = y - (getLayoutY() + getHeight() / 2);
        return ellipse.contains(localX, localY);
    }

    // Convert to a DTO for serialization
    public AttributeDTO toDTO() {
        return new AttributeDTO(posX, posY, ellipse.getRadiusX() * 2, ellipse.getRadiusY() * 2, textNode.getText(), strokeColor.toString(), strokeWidth);
    }

    // Convert from a DTO to an Attribute
    public static Attribute fromDTO(AttributeDTO dto) {
        Attribute attribute = new Attribute(dto.getPosX(), dto.getPosY(), dto.getHeight(), dto.getWidth());
        attribute.setTextNode(dto.getText());
        attribute.setStrokeColor(Color.valueOf(dto.getStrokeColor()));
        attribute.setStrokeWidth(dto.getStrokeWidth());
        return attribute;
    }

    // DTO class for Attribute
    public static class AttributeDTO {
        private double posX;
        private double posY;
        private double width;
        private double height;
        private String text;
        private String strokeColor;
        private double strokeWidth;

        public AttributeDTO(@JsonProperty("posX") double posX, @JsonProperty("posY") double posY, @JsonProperty("width") double width, @JsonProperty("height") double height, @JsonProperty("text") String text, @JsonProperty("strokeColor") String strokeColor, @JsonProperty("strokeWidth") double strokeWidth) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.text = text;
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
        }

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

        public String getText() {
            return text;
        }

        public String getStrokeColor() {
            return strokeColor;
        }

        public double getStrokeWidth() {
            return strokeWidth;
        }
    }
}
