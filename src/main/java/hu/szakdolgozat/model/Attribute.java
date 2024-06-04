package hu.szakdolgozat.model;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Attribute extends StackPane implements Selectable, Draggable {
    private double posX;
    private double posY;
    private Color strokeColor = Color.BLACK;
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
        this(posX, posY, 30, 50); // Default values for height and width
    }

    public Attribute(double posX, double posY, double height, double width) {

        ellipse = new Ellipse(posX,posY, width / 2, height / 2);

        ellipse.setFill(Color.WHITE);
        ellipse.setStroke(strokeColor);

        getChildren().add(ellipse);
        setTextNode("Attribute");

        setAlignment(ellipse, Pos.CENTER); // Center the ellipse within the StackPane
        setLayoutX(posX);
        setLayoutY(posY);

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
        ellipse.setStroke(strokeColor);
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
        double localX = x - getLayoutX();
        double localY = y - getLayoutY();
        return ellipse.contains(localX, localY);
    }
}
