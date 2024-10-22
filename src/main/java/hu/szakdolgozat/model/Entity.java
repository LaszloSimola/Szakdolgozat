package hu.szakdolgozat.model;

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
    private Rectangle outerRectangle;
    private Text textNode;
    private boolean isWeakEntity = false;
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
        this(0, 0, 100, 50,false); // Default values for position, width, and height
    }

    public Entity(double posX, double posY) {
        this(posX, posY, 100, 50,false); // Default values for width and height
    }

    public Entity(@JsonProperty("x") double posX, @JsonProperty("y") double posY, @JsonProperty("width") double width, @JsonProperty("height") double height, @JsonProperty("isWeakEntity")boolean isWeakEntity) {
        this.posX = posX;
        this.posY = posY;
        this.isWeakEntity = isWeakEntity;

        rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(strokeColor);
        rectangle.setStrokeWidth(strokeWidth);

        // Outer rectangle, initially hidden
        outerRectangle = new Rectangle(width + 10, height + 10); // Slightly larger
        outerRectangle.setFill(Color.TRANSPARENT);
        outerRectangle.setStroke(Color.BLACK);
        outerRectangle.setStrokeWidth(2);
        outerRectangle.setVisible(false); // Initially hidden

        // Add both rectangles to the StackPane, outer first so it is behind
        getChildren().addAll(outerRectangle, rectangle);
        setTextNode("Entity"); // Add the text node and set its initial text

        setAlignment(rectangle, Pos.CENTER); // Center the rectangle within the StackPane
        setLayoutX(posX);
        setLayoutY(posY);

        // Ensure that text is always centered
        widthProperty().addListener((obs, oldVal, newVal) -> setAlignment(textNode, Pos.CENTER));
        heightProperty().addListener((obs, oldVal, newVal) -> setAlignment(textNode, Pos.CENTER));
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
        outerRectangle.setStroke(strokeColor);
        rectangle.setStroke(strokeColor);
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        rectangle.setStrokeWidth(strokeWidth);
        outerRectangle.setStrokeWidth(strokeWidth);
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
            outerRectangle.setStroke(Color.RED);
            rectangle.setStroke(Color.RED);
        } else {
            outerRectangle.setStroke(strokeColor);
            rectangle.setStroke(strokeColor);
        }
        isSelected = selected;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    // Getter for isWeakEntity
    public boolean isWeakEntity() {
        return isWeakEntity;
    }

    // Setter for isWeakEntity
    public void setWeakEntity(boolean isWeakEntity) {
        this.isWeakEntity = isWeakEntity;
        outerRectangle.setVisible(isWeakEntity);
    }

    public void updateSize(double width, double height) {
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        outerRectangle.setWidth(width + 10);
        outerRectangle.setHeight(height + 10);
    }

    @Override
    public boolean contains(double x, double y) {
        return rectangle.contains(x - getLayoutX(), y - getLayoutY());
    }

    // Convert Entity to DTO
    public EntityDTO toDTO() {
        return new EntityDTO(posX, posY, rectangle.getWidth(), rectangle.getHeight(),
                textNode.getText(), strokeColor.toString(), strokeWidth, isWeakEntity);
    }

    // Convert DTO back to Entity
    public static Entity fromDTO(EntityDTO dto) {
        Entity entity = new Entity();
        entity.setPosX(dto.getPosX());
        entity.setPosY(dto.getPosY());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setTextNode(dto.getText());
        entity.setStrokeColor(Color.valueOf(dto.getStrokeColor()));
        entity.setStrokeWidth(dto.getStrokeWidth());
        entity.setWeakEntity(dto.isWeakEntity());
        return entity;
    }

    // DTO class
    public static class EntityDTO {
        private double posX;
        private double posY;
        private double width;
        private double height;
        private String text;
        private String strokeColor;
        private double strokeWidth;
        @JsonProperty("isWeakEntity")
        private boolean isWeakEntity;

        // No-argument constructor
        public EntityDTO() {
        }

        public EntityDTO(double posX, double posY, double width, double height, String text, String strokeColor, double strokeWidth, boolean isWeakEntity) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.text = text;
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
            this.isWeakEntity = isWeakEntity;
        }

        // Getters for DTO
        public double getPosX() { return posX; }
        public double getPosY() { return posY; }
        public double getWidth() { return width; }
        public double getHeight() { return height; }
        public String getText() { return text; }
        public String getStrokeColor() { return strokeColor; }
        public double getStrokeWidth() { return strokeWidth; }
        @JsonProperty("isWeakEntity")
        public boolean isWeakEntity() { return isWeakEntity; }
    }
}

