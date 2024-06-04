    package hu.szakdolgozat.model;

    import javafx.scene.paint.Color;
    import javafx.scene.shape.Polygon;

    public class Relation extends Polygon implements Selectable, Draggable {
        private double posX;
        private double posY;
        private boolean isSelected = false;

        public Relation(double... points) {
            super(points);
            setFill(Color.WHITE);
            setStroke(Color.BLACK);
            setSelected(true); // Assuming you want to initially select the relation
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

        @Override
        public boolean isSelected() {
            return isSelected;
        }

        @Override
        public void setSelected(boolean selected) {
            isSelected = selected;
            if (selected) {
                setStroke(Color.RED);
            } else {
                setStroke(Color.BLACK);
            }
        }

        @Override
        public void drag(double deltaX, double deltaY) {
            Double[] points = getPoints().toArray(new Double[0]);
            for (int i = 0; i < points.length; i += 2) {
                points[i] += deltaX;
                points[i + 1] += deltaY;
            }
            getPoints().setAll(points);
        }

    }
