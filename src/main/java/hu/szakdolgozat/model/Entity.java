package hu.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.Serializable;

public class Entity extends StackPane implements Serializable, Selectable, Draggable {
    private double posX;
    private double posY;
    private Color strokeColor = Color.BLACK;
    private double strokeWidth = 1.0;
    boolean isSelected = false;
    private Rectangle rectangle;
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

    public Entity() {
        this(0, 0, 100, 50); // Default values for position, width, and height
    }

    public Entity(double posX, double posY) {
        this(posX, posY, 100, 50); // Default values for width and height
    }

    public Entity(@JsonProperty("x") double posX, @JsonProperty("y") double posY, @JsonProperty("width") double width, @JsonProperty("height") double height) {
        this.posX = posX;
        this.posY = posY;

        rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(strokeColor);
        rectangle.setStrokeWidth(strokeWidth);

        getChildren().add(rectangle); // Add the rectangle first
        setTextNode("Entity"); // Add the text node and set its initial text

        setAlignment(rectangle, Pos.CENTER); // Center the rectangle within the StackPane
        setLayoutX(posX);
        setLayoutY(posY);

        // Ensure that text is always centered
        widthProperty().addListener((obs, oldVal, newVal) -> setAlignment(textNode, Pos.CENTER));
        heightProperty().addListener((obs, oldVal, newVal) -> setAlignment(textNode, Pos.CENTER));
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
        rectangle.setStroke(strokeColor);
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        rectangle.setStrokeWidth(strokeWidth);
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
            rectangle.setStroke(Color.RED);
        } else {
            rectangle.setStroke(strokeColor);
        }
        isSelected = selected;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public boolean contains(double x, double y) {
        return rectangle.contains(x - getLayoutX(), y - getLayoutY());
    }

    // Convert to a DTO for serialization
    public EntityDTO toDTO() {
        return new EntityDTO(posX, posY, rectangle.getWidth(), rectangle.getHeight(), textNode.getText(), strokeColor.toString(), strokeWidth);
    }

    // Convert from a DTO to an Entity
    public static Entity fromDTO(EntityDTO dto) {
        Entity entity = new Entity(dto.getPosX(), dto.getPosY(), dto.getWidth(), dto.getHeight());
        entity.setTextNode(dto.getText());
        entity.setStrokeColor(Color.valueOf(dto.getStrokeColor()));
        entity.setStrokeWidth(dto.getStrokeWidth());
        return entity;
    }

    // DTO class for Entity
    public static class EntityDTO {
        private double posX;
        private double posY;
        private double width;
        private double height;
        private String text;
        private String strokeColor;
        private double strokeWidth;

        public EntityDTO(@JsonProperty("posX") double posX, @JsonProperty("posY") double posY, @JsonProperty("width") double width, @JsonProperty("height") double height, @JsonProperty("text") String text, @JsonProperty("strokeColor") String strokeColor, @JsonProperty("strokeWidth") double strokeWidth) {
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
