package hu.szakdolgozat.controller;

import hu.szakdolgozat.model.Entity;
import hu.szakdolgozat.model.Relation;
import hu.szakdolgozat.view.AppView;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class ApplicationController {
    private boolean entityClicked = false;
    private boolean relationshipClicked = false;
    private boolean attributeClicked = false;

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

    public void setEntityClicked(boolean entityClicked) {
        this.entityClicked = entityClicked;
    }

    public void setRelationshipClicked(boolean relationshipClicked) {
        this.relationshipClicked = relationshipClicked;
    }

    public void setAttributeClicked(boolean attributeClicked) {
        this.attributeClicked = attributeClicked;
    }
}