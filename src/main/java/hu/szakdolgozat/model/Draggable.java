package hu.szakdolgozat.model;

public interface Draggable {
    double getPosX();
    void setPosX(double x);
    double getPosY();
    void setPosY(double y);

    void drag(double deltaX, double deltaY);
}
