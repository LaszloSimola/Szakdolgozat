package hu.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.Serializable;

public class OwnLine extends Line implements Selectable, Serializable {
    private String startNodeId;
    private String endNodeId;

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
    public void setStartNodeId(String startNodeId) {
        this.startNodeId = startNodeId;
    }

    public void setEndNodeId(String endNodeId) {
        this.endNodeId = endNodeId;
    }

    public String getStartNodeId() {
        return this.startNodeId;
    }

    public String getEndNodeId() {
        return endNodeId;
    }


    // DTO class for OwnLine
    public static class OwnLineDTO {
        private double startX;
        private double startY;
        private double endX;
        private double endY;
        private String startNodeId;
        private String endNodeId;

        public OwnLineDTO(@JsonProperty("startX") double startX, @JsonProperty("startY") double startY,
                          @JsonProperty("endX") double endX, @JsonProperty("endY") double endY,
                          @JsonProperty("startNodeId") String startNodeId, @JsonProperty("endNodeId") String endNodeId) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.startNodeId = startNodeId;
            this.endNodeId = endNodeId;

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

        public String getStartNodeId() {
            return startNodeId;
        }

        public String getEndNodeId() {
            return endNodeId;
        }
    }

    public OwnLineDTO toDTO() {
        return new OwnLineDTO(getStartX(), getStartY(), getEndX(), getEndY(), getStartNodeId(), getEndNodeId());
    }

    public static OwnLine fromDTO(OwnLineDTO dto) {
        OwnLine line = new OwnLine();
        line.setStartX(dto.getStartX());
        line.setStartY(dto.getStartY());
        line.setEndX(dto.getEndX());
        line.setEndY(dto.getEndY());
        line.setStartNodeId(dto.getStartNodeId());
        line.setEndNodeId(dto.getEndNodeId());
        return line;
    }
}
