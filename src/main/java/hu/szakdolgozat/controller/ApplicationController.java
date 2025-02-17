package hu.szakdolgozat.controller;

import hu.szakdolgozat.model.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import java.util.List;

/**
 * The {@code ApplicationController} class manages interactions between UI components
 * and handles button clicks for creating entities, relationships, attributes, and connections.
 */
public class ApplicationController {
    private Node selectedNode;
    private boolean entityClicked = false;
    private boolean relationshipClicked = false;
    private boolean attributeClicked = false;
    private boolean connectClicked = false;
    private boolean specializeClicked = false;
    private Button activeButton = null;

    /**
     * Handles the connection button click event.
     * Toggles the connect mode and resets other modes.
     */
    public void handleConnectButton() {
        relationshipClicked = false;
        attributeClicked = false;
        entityClicked = false;
        specializeClicked = false;
        connectClicked = !connectClicked;
    }

    /**
     * Handles the entity button click event.
     * Activates entity creation mode and deactivates other modes.
     */
    public void handleEntityButtonClick() {
        relationshipClicked = false;
        attributeClicked = false;
        specializeClicked = false;
        entityClicked = !entityClicked;
    }

    /**
     * Handles the relationship button click event.
     * Activates relationship creation mode and deactivates other modes.
     */
    public void handleRelationButtonClick() {
        entityClicked = false;
        attributeClicked = false;
        specializeClicked = false;
        relationshipClicked = !relationshipClicked;
    }

    /**
     * Handles the attribute button click event.
     * Activates attribute creation mode and deactivates other modes.
     */
    public void handleAttributeButtonClick() {
        entityClicked = false;
        relationshipClicked = false;
        specializeClicked = false;
        attributeClicked = !attributeClicked;
    }

    /**
     * Handles the specialization button click event.
     * Activates specialization mode and deactivates other modes.
     */
    public void handleSpecializeButtonClick() {
        entityClicked = false;
        relationshipClicked = false;
        attributeClicked = false;
        specializeClicked = !specializeClicked;
    }

    /**
     * Toggles the styling of a button when clicked.
     * Ensures only one button is styled as active at a time.
     *
     * @param button The button to toggle.
     * @param styleClass The style class to apply.
     */
    public void toggleButtonStyle(Button button, String styleClass) {
        if (activeButton == button) {
            button.getStyleClass().remove(styleClass);
            activeButton = null;
        } else {
            if (activeButton != null) {
                activeButton.getStyleClass().remove(styleClass);
            }
            button.getStyleClass().add(styleClass);
            activeButton = button;
        }
    }

    // Getters and Setters

    public boolean isEntityClicked() { return entityClicked; }
    public void setEntityClicked(boolean entityClicked) { this.entityClicked = entityClicked; }

    public boolean isRelationshipClicked() { return relationshipClicked; }
    public void setRelationshipClicked(boolean relationshipClicked) { this.relationshipClicked = relationshipClicked; }

    public boolean isAttributeClicked() { return attributeClicked; }
    public void setAttributeClicked(boolean attributeClicked) { this.attributeClicked = attributeClicked; }

    public boolean isConnectClicked() { return connectClicked; }
    public void setConnectClicked(boolean connectClicked) { this.connectClicked = connectClicked; }

    public boolean isSpecializeClicked() { return specializeClicked; }
    public void setSpecializeClicked(boolean specializeClicked) { this.specializeClicked = specializeClicked; }

    /**
     * Connects two nodes with a line and optionally adds arrowheads at the ends.
     *
     * @param startNode The starting node.
     * @param endNode The ending node.
     * @param root The pane to which the line is added.
     * @param lines The list of existing lines.
     * @param arrowAtEnd Whether to add an arrow at the end of the line.
     * @param arrowAtStart Whether to add an arrow at the start of the line.
     */
    public void connectNodes(Node startNode, Node endNode, Pane root, List<OwnLine> lines, boolean arrowAtEnd, boolean arrowAtStart) {
        OwnLine line = new OwnLine();

        line.startXProperty().bind(Bindings.createDoubleBinding(() -> getEdgePoint(startNode, endNode)[0], startNode.boundsInParentProperty(), endNode.boundsInParentProperty()));
        line.startYProperty().bind(Bindings.createDoubleBinding(() -> getEdgePoint(startNode, endNode)[1], startNode.boundsInParentProperty(), endNode.boundsInParentProperty()));
        line.endXProperty().bind(Bindings.createDoubleBinding(() -> getEdgePoint(endNode, startNode)[0], startNode.boundsInParentProperty(), endNode.boundsInParentProperty()));
        line.endYProperty().bind(Bindings.createDoubleBinding(() -> getEdgePoint(endNode, startNode)[1], startNode.boundsInParentProperty(), endNode.boundsInParentProperty()));

        root.getChildren().add(line);
        lines.add(line);
        line.toBack();

        if (arrowAtEnd) {
            Arrow arrowEnd = new Arrow();
            arrowEnd.startXProperty().bind(line.endXProperty());
            arrowEnd.startYProperty().bind(line.endYProperty());
            arrowEnd.endXProperty().bind(line.startXProperty());
            arrowEnd.endYProperty().bind(line.startYProperty());
            root.getChildren().add(arrowEnd);
        }

        if (arrowAtStart) {
            Arrow arrowStart = new Arrow();
            arrowStart.startXProperty().bind(line.startXProperty());
            arrowStart.startYProperty().bind(line.startYProperty());
            arrowStart.endXProperty().bind(line.endXProperty());
            arrowStart.endYProperty().bind(line.endYProperty());
            root.getChildren().add(arrowStart);
        }
    }

    /**
     * Computes the connection point between two nodes.
     *
     * @param from The starting node.
     * @param to The target node.
     * @return An array containing the x and y coordinates of the edge point.
     */
    private double[] getEdgePoint(Node from, Node to) {
        Bounds fromBounds = from.getBoundsInParent();
        Bounds toBounds = to.getBoundsInParent();

        double centerX = fromBounds.getMinX() + fromBounds.getWidth() / 2;
        double centerY = fromBounds.getMinY() + fromBounds.getHeight() / 2;
        double toCenterX = toBounds.getMinX() + toBounds.getWidth() / 2;
        double toCenterY = toBounds.getMinY() + toBounds.getHeight() / 2;

        double dx = toCenterX - centerX;
        double dy = toCenterY - centerY;
        double angle = Math.atan2(dy, dx);

        double edgeX = centerX + (fromBounds.getWidth() / 2) * Math.cos(angle);
        double edgeY = centerY + (fromBounds.getHeight() / 2) * Math.sin(angle);

        return new double[]{edgeX, edgeY};
    }
}
