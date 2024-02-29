package hu.szakdolgozat.controller;

import hu.szakdolgozat.model.Entity;
import hu.szakdolgozat.view.AppView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ApplicationController {
    private Entity currentEntity;
    private boolean entityClicked = false;
    private boolean relationshipClicked = false;
    // Add other model components as needed

    public void handleEgyedButtonClick() {
        if (!entityClicked) {
            entityClicked = true;
        } else {
            entityClicked = false;
        }
    }

    public void handleKapcsolatButtonClick() {
        entityClicked = false;
        if (!relationshipClicked) {
            relationshipClicked = true;
        } else {
            relationshipClicked = false;
        }
    }

    public boolean isEntityClicked() {
        return entityClicked;
    }

    public boolean isRelationshipClicked() {
        return relationshipClicked;
    }

    public Entity handleSceneClick(MouseEvent event) {
        if (entityClicked) {
            // Create and add entity
            return new Entity(event.getX(), event.getY(), 50, 30);
        }
        if (relationshipClicked) {
            return null;
        }
        return null;
    }
}