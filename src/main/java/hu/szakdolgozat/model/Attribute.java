package hu.szakdolgozat.model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Attribute extends Ellipse implements Selectable,Draggable{

    private double posX;
    private double posY;
    private int height,width;

    boolean isSelected = false;

    public Attribute() {
    }

    public Attribute(double posX, double posY) {
        this(posX, posY, 30, 50);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setFill(Color.WHITE);
        this.setSelected(true);
    }

    public Attribute(double posX, double posY, int height, int width) {
        super(posX,posY , width, height);
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
        setCenterX(x);
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double y) {
        posY = y;
        setCenterY(y);
    }

    @Override
    public void drag(double deltaX, double deltaY) {
        setPosX(getPosX() + deltaX);
        setPosY(getPosY() + deltaY);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
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
}
