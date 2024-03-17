// View
package hu.szakdolgozat.view;

import hu.szakdolgozat.controller.ApplicationController;
import hu.szakdolgozat.model.Attribute;
import hu.szakdolgozat.model.Entity;
import hu.szakdolgozat.model.Relation;
import javafx.application.Application;
import javafx.css.Style;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class AppView extends Application {
    private final ApplicationController controller = new ApplicationController();
    private Node selectedNode;
    private double mouseDownX ;
    private double mouseDownY ;


    @Override
    public void start(Stage stage) {

        // buttons
        Button egyedButton = new Button("Egyed");
        Button kapcsolatButton = new Button("Kapcsolat");
        Button attributumButton = new Button("Attributum");

        //style for the buttons
        egyedButton.getStyleClass().add("action-button");
        kapcsolatButton.getStyleClass().add("action-button");
        attributumButton.getStyleClass().add("action-button");


        // Event handlers for buttons
        egyedButton.setOnAction(e -> controller.handleEntityButtonClick());
        kapcsolatButton.setOnAction(e -> controller.handleRelationButtonClick());
        attributumButton.setOnAction(e->controller.handleAttributeButtonClick());

        // Vbox for the buttons
        VBox buttonPanel = new VBox(10);
        buttonPanel.getChildren().addAll(egyedButton, kapcsolatButton, attributumButton);

        //style for the buttonpanel
        buttonPanel.getStyleClass().add("button-panel");

        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(buttonPanel);
        BorderPane.setMargin(buttonPanel, new Insets(0, 0, 0, 0));


        Pane root = new Pane();
        borderPane.setCenter(root);

        root.getStyleClass().add("root");

        Scene scene = new Scene(borderPane, 700, 500);

        // Load css stylesheet
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        }catch (Exception e){
            System.err.println("Wrong path given!");
        }


        root.setOnMouseClicked(event -> {
            double clickX = event.getX();
            double clickY = event.getY();

            double rootClickX = clickX;
            double rootClickY = clickY;

            // make an entity to the scene
            if (controller.isEntityClicked()) {
                System.out.println("clicked");
                Entity entity = new Entity(rootClickX, rootClickY);
                selectedNode = entity;
                ((Entity)selectedNode).setSelected(true);
                controller.setEntityClicked(false);
                root.getChildren().add(entity);

                //deselector for entities
                if (selectedNode != null) {
                    for (Node node : root.getChildren()) {
                        if (node instanceof Entity && node != selectedNode){
                            ((Entity) node).setSelected(false);
                            selectedNode = null;
                            System.out.println("others deselected");
                        }
                    }
                }
            }
            // selector for existing entities
            for (Node node : root.getChildren()) {
                if (!controller.isEntityClicked()) {
                    if (node instanceof Entity && node.contains(rootClickX, rootClickY)) {
                        selectedNode = node;
                        System.out.println("got selected this");
                        ((Entity) node).setSelected(true);
                    } else {
                        assert node instanceof Entity;
                        ((Entity) node).setSelected(false);
                        System.out.println("other deselect");
                    }
                }
            }

            if (controller.isRelationshipClicked()) {
                Relation polygon = new Relation(
                        rootClickX, rootClickY,
                        rootClickX + 50, rootClickY - 25,
                        rootClickX + 100, rootClickY,
                        rootClickX + 50, rootClickY + 25
                );
                root.getChildren().add(polygon);
            }
            if (controller.isAttributeClicked()) {
                Attribute attribute = new Attribute(rootClickX, rootClickY);
                attribute.setCenterX(rootClickX);
                attribute.setCenterY(rootClickY);
                attribute.setRadiusX(70);
                attribute.setRadiusY(40);
                attribute.setFill(Color.TRANSPARENT); // Set fill color to transparent
                attribute.setStroke(Color.BLACK);
                root.getChildren().add(attribute);
            }
        });

        //seton mousepressed event implementation
        root.setOnMousePressed(event -> {
                if (!controller.isEntityClicked()) {
                    for (Node node : root.getChildren()) {
                        if (node instanceof Entity && node.contains(event.getX(), event.getY())) {
                            selectedNode = node;
                            ((Entity) selectedNode).setSelected(true);
                            mouseDownX = event.getX();
                            mouseDownY = event.getY();
                            ((Entity) selectedNode).setX(mouseDownX);
                            ((Entity) selectedNode).setY(mouseDownY);
                            System.out.println("Entity clicked.");
                        }
                    }
                }if (selectedNode != null){
                selectedNode.setOnMouseDragged(e -> {
                    if (selectedNode != null && selectedNode instanceof Entity) {
                        double deltaX = e.getX() - mouseDownX;
                        double deltaY = e.getY() - mouseDownY;
                        ((Entity) selectedNode).setX(((Entity) selectedNode).getX() + deltaX);
                        ((Entity) selectedNode).setY(((Entity) selectedNode).getY() + deltaY);
                        mouseDownX = e.getX();
                        mouseDownY = e.getY();
                        System.out.println("dragged");
                    }
                });
            }
        });


        // Set the scene to the stage
        stage.setTitle("Ek_editor");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }

}