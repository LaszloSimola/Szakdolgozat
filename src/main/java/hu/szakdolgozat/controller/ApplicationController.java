package hu.szakdolgozat.controller;

import hu.szakdolgozat.model.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.List;

public class ApplicationController {
    private Node selectedNode;
    private boolean entityClicked = false;
    private boolean relationshipClicked = false;
    private boolean attributeClicked = false;
    private boolean connectClicked = false;
    private boolean specializeClicked = false;

    public void handleConnectButton(){
        relationshipClicked = false;
        attributeClicked = false;
        entityClicked = false;
        specializeClicked = false;
        if (connectClicked){
            connectClicked = false;
        }else{
            connectClicked = true;
        }

    }
    //handle buttons
    public void handleEntityButtonClick() {
        relationshipClicked = false;
        attributeClicked = false;
        specializeClicked = false;
        if (!entityClicked) {
            entityClicked = true;
        } else {
            entityClicked = false;
        }
    }
    public void handleRelationButtonClick() {
        entityClicked = false;
        attributeClicked = false;
        specializeClicked = false;
        if (!relationshipClicked) {
            relationshipClicked = true;
        } else {
            relationshipClicked = false;
        }
    }
    public void handleAttributeButtonClick() {
        entityClicked = false;
        relationshipClicked = false;
        specializeClicked = false;
        if (!attributeClicked) {
            attributeClicked = true;
        } else {
            attributeClicked = false;
        }
    }
    public void handleSpecializeButtonClick() {
        entityClicked = false;
        relationshipClicked = false;
        attributeClicked = false;
        if (!specializeClicked) {
            specializeClicked = true;
            System.out.println("clicked");
        } else {
            specializeClicked = false;
        }
    }

    //getters
    public boolean isEntityClicked() {
        return entityClicked;
    }
    public boolean isRelationshipClicked() {
        return relationshipClicked;
    }
    public boolean isAttributeClicked() {
        return attributeClicked;
    }

    public boolean isConnectClicked() {
        return connectClicked;
    }

    public void setEntityClicked(boolean entityClicked) {
        this.entityClicked = entityClicked;
    }

    public void setRelationshipClicked(boolean relationshipClicked) {
        this.relationshipClicked = relationshipClicked;
    }

    public void setAttributeClicked(boolean attributeClicked) {
        this.attributeClicked = attributeClicked;
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

    public void connectNodes(Node startNode, Node endNode, Pane root, List<OwnLine> lines) {
        // Draw a line between startNode and endNode
        OwnLine line = new OwnLine();
        line.startXProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds bounds = startNode.getBoundsInParent();
            return bounds.getMinX() + bounds.getWidth() / 2;
        }, startNode.boundsInParentProperty()));

        line.startYProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds bounds = startNode.getBoundsInParent();
            return bounds.getMinY() + bounds.getHeight() / 2;
        }, startNode.boundsInParentProperty()));

        line.endXProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds bounds = endNode.getBoundsInParent();
            return bounds.getMinX() + bounds.getWidth() / 2;
        }, endNode.boundsInParentProperty()));

        line.endYProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds bounds = endNode.getBoundsInParent();
            return bounds.getMinY() + bounds.getHeight() / 2;
        }, endNode.boundsInParentProperty()));

        root.getChildren().add(line);
        lines.add(line);
        line.toBack();

    }


}
