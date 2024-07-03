package hu.szakdolgozat.model;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class OwnLine extends Line implements Selectable, Serializable {
    private Node startNode;
    private Node endNode;

    public OwnLine() {
        this.setStrokeWidth(1.5);
        this.toBack();
    }

    boolean isSelected = false;

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            this.toBack();
            this.setStroke(Color.RED);
        } else {
            this.setStroke(Color.BLACK);
        }
        isSelected = selected;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    // DTO class for OwnLine
    public static class OwnLineDTO {
        private double startX;
        private double startY;
        private double endX;
        private double endY;

        private Node startNode;
        private Node endNode;

        public OwnLineDTO(@JsonProperty("startX") double startX, @JsonProperty("startY") double startY,
                          @JsonProperty("endX") double endX, @JsonProperty("endY") double endY,
                          @JsonProperty("startNode") Node startNode,@JsonProperty("endNode") Node endNode) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.startNode = startNode;
            this.endNode = endNode;
        }

        public double getStartX() {
            return startX;
        }

        public double getStartY() {
            return startY;
        }

        public double getEndX() {
            return endX;
        }

        public double getEndY() {
            return endY;
        }


        public Node getStartNode() {
            return startNode;
        }


        public Node getEndNode() {
            return endNode;
        }
    }

    public OwnLineDTO toDTO() {
        return new OwnLineDTO(getStartX(), getStartY(), getEndX(), getEndY(),getStartNode(),getEndNode());
    }

    public static OwnLine fromDTO(OwnLineDTO dto) {
        OwnLine line = new OwnLine();
        line.setStartX(dto.getStartX());
        line.setStartY(dto.getStartY());
        line.setEndX(dto.getEndX());
        line.setEndY(dto.getEndY());
        line.setStartNode(dto.getStartNode());
        line.setEndNode(dto.getEndNode());
        return line;
    }
}
