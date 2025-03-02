package hu.szakdolgozat.controller;

import hu.szakdolgozat.model.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    // Fields
    private Node selectedNode;
    private Button activeButton;
    private List<Arrow> lines;

    // Mode flags
    private boolean entityClicked;
    private boolean relationshipClicked;
    private boolean attributeClicked;
    private boolean connectClicked;
    private boolean specializeClicked;

    // Constructor
    public ApplicationController() {
        this.lines = new ArrayList<>();
        resetModes();
    }

    // Mode handling methods
    public void handleConnectButton() {
        resetModes();
        connectClicked = true;
    }

    public void handleEntityButtonClick() {
        resetModes();
        entityClicked = true;
    }

    public void handleRelationButtonClick() {
        resetModes();
        relationshipClicked = true;
    }

    public void handleAttributeButtonClick() {
        resetModes();
        attributeClicked = true;
    }

    public void handleSpecializeButtonClick() {
        resetModes();
        specializeClicked = true;
    }

    private void resetModes() {
        entityClicked = false;
        relationshipClicked = false;
        attributeClicked = false;
        connectClicked = false;
        specializeClicked = false;
    }

    // UI methods
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

    // Connection methods
    public void connectNodes(Node startNode, Node endNode, Pane root, List<Arrow> lines, boolean arrowAtEnd, boolean arrowAtStart) {
        Arrow line = createArrow(startNode, endNode);
        configureArrow(line, arrowAtEnd, arrowAtStart);
        addArrowToScene(line, root, lines);
    }

    private Arrow createArrow(Node startNode, Node endNode) {
        Arrow line = new Arrow();
        bindArrowProperties(line, startNode, endNode);
        return line;
    }

    private void bindArrowProperties(Arrow line, Node startNode, Node endNode) {
        line.startXProperty().bind(Bindings.createDoubleBinding(() -> getEdgePoint(startNode, endNode)[0], startNode.boundsInParentProperty(), endNode.boundsInParentProperty()));
        line.startYProperty().bind(Bindings.createDoubleBinding(() -> getEdgePoint(startNode, endNode)[1], startNode.boundsInParentProperty(), endNode.boundsInParentProperty()));
        line.endXProperty().bind(Bindings.createDoubleBinding(() -> getEdgePoint(endNode, startNode)[0], startNode.boundsInParentProperty(), endNode.boundsInParentProperty()));
        line.endYProperty().bind(Bindings.createDoubleBinding(() -> getEdgePoint(endNode, startNode)[1], startNode.boundsInParentProperty(), endNode.boundsInParentProperty()));
    }

    private void configureArrow(Arrow line, boolean arrowAtEnd, boolean arrowAtStart) {
        line.setArrowAtStart(arrowAtStart);
        line.setArrowAtEnd(arrowAtEnd);
    }

    private void addArrowToScene(Arrow line, Pane root, List<Arrow> lines) {
        root.getChildren().add(line);
        lines.add(line);
        this.lines.add(line);
        line.toBack();
    }

    public boolean areNodesConnected(Node node1, Node node2,List<Arrow> lines) {
        for (Arrow arrow : lines) {
            if (isArrowConnectingNodes(arrow, node1, node2)) {
                return true;
            }
        }
        return false;
    }

    private boolean isArrowConnectingNodes(Arrow arrow, Node node1, Node node2) {
        double[] point1 = getEdgePoint(node1, node2);
        double[] point2 = getEdgePoint(node2, node1);
        return (arrow.getStartX() == point1[0] && arrow.getStartY() == point1[1] &&
                arrow.getEndX() == point2[0] && arrow.getEndY() == point2[1]) ||
                (arrow.getStartX() == point2[0] && arrow.getStartY() == point2[1] &&
                        arrow.getEndX() == point1[0] && arrow.getEndY() == point1[1]);
    }

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

    // Getters and Setters

    public Node getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(Node selectedNode) {
        this.selectedNode = selectedNode;
    }

    public Button getActiveButton() {
        return activeButton;
    }

    public void setActiveButton(Button activeButton) {
        this.activeButton = activeButton;
    }

    public List<Arrow> getLines() {
        return lines;
    }

    public void setLines(List<Arrow> lines) {
        this.lines = lines;
    }

    public boolean isEntityClicked() {
        return entityClicked;
    }

    public void setEntityClicked(boolean entityClicked) {
        this.entityClicked = entityClicked;
    }

    public boolean isRelationshipClicked() {
        return relationshipClicked;
    }

    public void setRelationshipClicked(boolean relationshipClicked) {
        this.relationshipClicked = relationshipClicked;
    }

    public boolean isAttributeClicked() {
        return attributeClicked;
    }

    public void setAttributeClicked(boolean attributeClicked) {
        this.attributeClicked = attributeClicked;
    }

    public boolean isConnectClicked() {
        return connectClicked;
    }

    public void setConnectClicked(boolean connectClicked) {
        this.connectClicked = connectClicked;
    }

    public boolean isSpecializeClicked() {
        return specializeClicked;
    }

    public void setSpecializeClicked(boolean specializeClicked) {
        this.specializeClicked = specializeClicked;
    }
}
