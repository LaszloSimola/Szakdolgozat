package hu.szakdolgozat.model;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Entity extends StackPane implements Selectable, Draggable {
    private double posX;
    private double posY;
    private Color strokeColor = Color.BLACK;

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

    public Entity(double posX, double posY, double width, double height) {
        this.posX = posX;
        this.posY = posY;

        rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(strokeColor);

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
}
