// View
package hu.szakdolgozat.view;

import hu.szakdolgozat.controller.ApplicationController;
import hu.szakdolgozat.model.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.css.Style;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
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
        Button deleteButton = new Button("Delete");

        //style for the buttons
        egyedButton.getStyleClass().add("action-button");
        kapcsolatButton.getStyleClass().add("action-button");
        attributumButton.getStyleClass().add("action-button");
        deleteButton.getStyleClass().add("delete-button");




        // Event handlers for buttons
        egyedButton.setOnAction(e -> controller.handleEntityButtonClick());
        kapcsolatButton.setOnAction(e -> controller.handleRelationButtonClick());
        attributumButton.setOnAction(e -> controller.handleAttributeButtonClick());

        // Vbox for the buttons
        VBox buttonPanel = new VBox(10);
        buttonPanel.getChildren().addAll(egyedButton, kapcsolatButton, attributumButton, deleteButton);
        buttonPanel.setPadding(new Insets(7));

        //style for the buttonpanel
        buttonPanel.getStyleClass().add("button-panel");

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(buttonPanel);

        Pane root = new Pane();
        borderPane.setCenter(root);
        root.getStyleClass().add("root");

        Scene scene = new Scene(borderPane, 700, 500);

        /*
         * vonal rajzolas és összekkötés megvalósítása és a többi elemre megcsinalni amit erre
         * draggeles vonalrajzolasnal a lekattintott ponttol
         * */

        // Load css stylesheet
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        }catch (Exception e){
            System.err.println("Wrong path given!");
        }

        deleteButton.setOnAction(e -> {
            if (selectedNode != null) {
                root.getChildren().remove(selectedNode);
                selectedNode = null;
            }
        });


        root.setOnMousePressed(event -> {
            mouseDownX = event.getX();
            mouseDownY = event.getY();
            double clickX = event.getX();
            double clickY = event.getY();

                // Deselect others at the same time
                for (Node n : root.getChildren()) {
                    if (n != selectedNode) {
                        if (n instanceof Selectable) {
                            ((Selectable) n).setSelected(false);
                        }
                    }
                }

            if (controller.isEntityClicked()){
                // Create a new entity
                Entity entity = new Entity(clickX, clickY);
                root.getChildren().add(entity);
                entity.setSelected(true);
                selectedNode = entity;

                // Deselect all other nodes
                for (Node node : root.getChildren()) {
                    if (node instanceof Selectable && node != entity) {
                        ((Selectable) node).setSelected(false);
                        System.out.println("deselected");
                    }
                }
                controller.setEntityClicked(false);

            }else if(controller.isAttributeClicked()){
                Attribute attribute = new Attribute(clickX,clickY);
                root.getChildren().add(attribute);
                attribute.setSelected(true);
                selectedNode = attribute;
                // Deselect all other nodes
                for (Node node : root.getChildren()) {
                    if (node instanceof Selectable && node != attribute) {
                        ((Selectable) node).setSelected(false);
                        System.out.println("deselected");
                    }
                }
                controller.setAttributeClicked(false);
            } else if (controller.isRelationshipClicked()) {
                double size = 70;
                double[] points = {
                        clickX, clickY - size / 2,
                        clickX + size / 2, clickY,
                        clickX, clickY + size / 2,
                        clickX - size / 2, clickY
                };
                Relation diamond = new Relation(points);
                root.getChildren().add(diamond);
                selectedNode = diamond;
                for (Node node : root.getChildren()) {
                    if (node instanceof Selectable && node != diamond) {
                        ((Selectable) node).setSelected(false);
                        System.out.println("deselected");
                    }
                }
                controller.setRelationshipClicked(false);
            }else{
                if (selectedNode != null) {
                    // Check if a second node is clicked
                    for (Node node : root.getChildren()) {
                        if (node != selectedNode && node.contains(clickX, clickY)) {
                            // Draw a line between selectedNode and node
                            Line line = new Line();
                            line.startXProperty().bind(Bindings.createDoubleBinding(() -> {
                                Bounds bounds = selectedNode.getBoundsInParent();
                                return bounds.getMinX() + bounds.getWidth() / 2;
                            }, selectedNode.boundsInParentProperty()));

                            line.startYProperty().bind(Bindings.createDoubleBinding(() -> {
                                Bounds bounds = selectedNode.getBoundsInParent();
                                return bounds.getMinY() + bounds.getHeight() / 2;
                            }, selectedNode.boundsInParentProperty()));

                            line.endXProperty().bind(Bindings.createDoubleBinding(() -> {
                                Bounds bounds = node.getBoundsInParent();
                                return bounds.getMinX() + bounds.getWidth() / 2;
                            }, node.boundsInParentProperty()));

                            line.endYProperty().bind(Bindings.createDoubleBinding(() -> {
                                Bounds bounds = node.getBoundsInParent();
                                return bounds.getMinY() + bounds.getHeight() / 2;
                            }, node.boundsInParentProperty()));

                            root.getChildren().add(line);
                            break;
                        }
                    }
                }
                    // selector for nodes (start from the last node and end at the first)
                    for (int i = root.getChildren().size() - 1; i >= 0; i--) {
                        Node node = root.getChildren().get(i);
                        if (node.contains(clickX, clickY)) {
                            if (node instanceof Selectable) {
                                selectedNode = node;
                                ((Selectable) selectedNode).setSelected(true);
                                System.out.println("node pressed.");
                                System.out.println(selectedNode);
                            }

                            // Deselect others at the same time
                            for (Node n : root.getChildren()) {
                                if (n != selectedNode) {
                                    if (n instanceof Selectable) {
                                        ((Selectable) n).setSelected(false);
                                    }
                                }
                            }
                            break;
                        }else{
                            selectedNode = null;
                            for (Node deselectednodes: root.getChildren()) {
                                if (deselectednodes instanceof Selectable) {
                                    ((Selectable) deselectednodes).setSelected(false);
                                }
                            }
                        }
                    }

                    if (selectedNode != null) {
                        selectedNode.toFront();
                    }
                }
        });

        root.setOnMouseDragged(event -> {
            // If a draggable node is selected, drag it
            double deltaX = event.getX() - mouseDownX;
            double deltaY = event.getY() - mouseDownY;
            if (selectedNode != null && selectedNode instanceof Draggable) {
                ((Draggable) selectedNode).drag(deltaX,deltaY);
                mouseDownX = event.getX();
                mouseDownY = event.getY();
                System.out.println("Node Dragged");
            }
        });

        //root.setOnMouseReleased(event -> {
        //    // Deselect the entity
        //    if (selectedNode != null && selectedNode instanceof Entity) {
        //        ((Entity) selectedNode).setSelected(false);
        //        selectedNode = null;
        //        System.out.println("Entity released.");
        //    }
        //});

        // Set the scene to the stage
        stage.setTitle("Ek_editor");
        stage.setScene(scene);
        stage.show();


    }



    public static void main(String[] args) {
        launch();
    }

}