package hu.szakdolgozat.model;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Entity extends Rectangle implements Selectable,Draggable {
    private double posX;
    private double posY;

    boolean isSelected = false;

    public Entity() {
    }

    public Entity(double posX, double posY) {
        this(posX, posY, 100, 50); // Default values for width and height
        this.setPosX(posX);
        this.setPosY(posY);
        this.setFill(Color.WHITE);
        this.setSelected(true);
    }


    public Entity(double posX, double posY, double width, double height) {
        super(posX, posY, width, height);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setFill(Color.WHITE);
        this.setStroke(Color.BLACK);
        this.setSelected(true);
    }


    public double getPosX() {
        return posX;
    }

    public void setPosX(double x) {
        posX = x;
        super.setX(x);
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double y) {
        posY = y;
        super.setY(y);
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
            this.setStroke(Color.RED);
        } else {
            this.setStroke(Color.BLACK);
        }
        isSelected = selected;
    }
}