package hu.szakdolgozat.controller;

import hu.szakdolgozat.model.*;
import hu.szakdolgozat.view.AppView;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    private Node selectedNode;
    private boolean entityClicked = false;
    private boolean relationshipClicked = false;
    private boolean attributeClicked = false;
    private boolean connectClicked = false;

    public void handleConnectButton(){
        relationshipClicked = false;
        attributeClicked = false;
        entityClicked = false;
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
        if (!entityClicked) {
            entityClicked = true;
        } else {
            entityClicked = false;
        }
    }
    public void handleRelationButtonClick() {
        entityClicked = false;
        attributeClicked = false;
        if (!relationshipClicked) {
            relationshipClicked = true;
        } else {
            relationshipClicked = false;
        }
    }
    public void handleAttributeButtonClick() {
        entityClicked = false;
        relationshipClicked = false;
        if (!attributeClicked) {
            attributeClicked = true;
        } else {
            attributeClicked = false;
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

    public void connectNodes(Node startNode, Node endNode, Pane root,List<OwnLine> lines) {
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
