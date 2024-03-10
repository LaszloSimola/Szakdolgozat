package hu.szakdolgozat.model;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Entity extends Rectangle {
    private double posX;
    private double posY;
    private int height,width;

    boolean isSelected = false;

    public Entity() {
    }
    public Entity(double posX, double posY) {
        this(posX, posY, 100, 50); // Default values for width and height
        this.setPosX(posX);
        this.setPosY(posY);
        this.setFill(Color.TRANSPARENT);
        this.setSelected(true);
    }


    public Entity(double posX, double posY, double width, double height) {
        super(posX,posY , width, height);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
        this.setSelected(true);
    }


    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        if (selected){
            this.setStroke(Color.RED);
        }else{
            this.setStroke(Color.BLACK);
        }
        isSelected = selected;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
