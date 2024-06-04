// View
package hu.szakdolgozat.view;

import hu.szakdolgozat.controller.ApplicationController;
import hu.szakdolgozat.model.*;
import javafx.application.Application;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AppView extends Application {
    private final ApplicationController controller = new ApplicationController();
    private Node selectedNode;
    private Node one, other;
    private double mouseDownX ;
    private double mouseDownY ;
    private boolean isConnectButtonVisible = false;
    List<OwnLine> lines = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        // buttons
        Button egyedButton = new Button("Egyed");
        Button kapcsolatButton = new Button("Kapcsolat");
        Button attributumButton = new Button("Attributum");
        Button deleteButton = new Button("Delete");
        Button connectButton = new Button("Connect");
        Button checkbutton = new Button("Check the lines");

        //style for the buttons
        egyedButton.getStyleClass().add("action-button");
        kapcsolatButton.getStyleClass().add("action-button");
        attributumButton.getStyleClass().add("action-button");
        connectButton.getStyleClass().add("connect-button");
        deleteButton.getStyleClass().add("delete-button");
        checkbutton.getStyleClass().add("action-button");


        // Event handlers for buttons
        egyedButton.setOnAction(e -> controller.handleEntityButtonClick());
        kapcsolatButton.setOnAction(e -> controller.handleRelationButtonClick());
        attributumButton.setOnAction(e -> controller.handleAttributeButtonClick());
        connectButton.setOnAction(e -> {
            controller.handleConnectButton();
            if (controller.isConnectClicked()){
                connectButton.getStyleClass().add("connect-button-clicked");
            }else{
                connectButton.getStyleClass().remove("connect-button-clicked");
            }
            isConnectButtonVisible = !isConnectButtonVisible;
        });


        // Vbox for the buttons
        VBox buttonPanel = new VBox(10);
        buttonPanel.getChildren().addAll(egyedButton, kapcsolatButton, attributumButton, deleteButton,connectButton,checkbutton);
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
         * méretezé és szin egyebek modositása
         * */

        // Load css
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        }catch (Exception e){
            System.err.println("Wrong path given!");
        }

        deleteButton.setOnAction(e -> {
            boolean bounded = false;
            if (selectedNode != null) {
                if (selectedNode instanceof Line){
                    lines.remove(selectedNode);
                    root.getChildren().remove(selectedNode);
                    selectedNode = null;
                } else if (selectedNode instanceof Draggable) {
                    for (Node node : root.getChildren()) {
                        if (node instanceof OwnLine) {
                            OwnLine line = (OwnLine) node;
                            if (LineChecker.isLineEndBoundToEntity(line, selectedNode)) {
                                line.startXProperty().unbind();
                                line.startYProperty().unbind();
                            }
                        }
                    }
                    root.getChildren().remove(selectedNode);
                    selectedNode = null;

                    // if line is not connected to a node then it should be deleted, after ane entity was deleted
                    Iterator<OwnLine> iterator = lines.iterator();
                    while (iterator.hasNext()) {
                        Line line = iterator.next();
                        if (LineChecker.isBothEndsBound(line)) {
                            System.out.println("Both ends are bound.");
                        } else {
                            root.getChildren().remove(line);
                            iterator.remove();
                        }
                    }

                }
            }
        });

        // ha le van totolve egy valami akkor automata törles vonalra
        // hibak osszeszedése amik lehetnek vonalvégek haromszog, öröklő kapcsolat
        // kulcsok gyenge egyed stackpane
        // szoveg, kimentés xml v json

        checkbutton.setOnAction(e -> {
            Iterator<OwnLine> iterator = lines.iterator();
            while (iterator.hasNext()) {
                Line line = iterator.next();
                if (LineChecker.isBothEndsBound(line)) {
                    System.out.println("Both ends are bound.");
                } else {
                    root.getChildren().remove(line);
                    iterator.remove();
                }
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

            if (controller.isEntityClicked()) {
                // Create a new entity
                Entity entity = new Entity(clickX, clickY, 100, 50);
                root.getChildren().add(entity);
                entity.setSelected(true);
                selectedNode = entity;
                entity.setTextNode("Entity");

                // Deselect all other nodes
                for (Node node : root.getChildren()) {
                    if (node instanceof Selectable && node != entity) {
                        ((Selectable) node).setSelected(false);
                    }
                }
                controller.setEntityClicked(false);
            }else if(controller.isAttributeClicked()){
                Attribute attribute = new Attribute(clickX,clickY,50,115);
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
            }else if(controller.isConnectClicked()){
                for (Node node : root.getChildren()) {
                    if (!(node instanceof Line)){
                        if (node.contains(clickX, clickY)) {
                            if (one == null) {
                                one = node;
                                break;
                            } else if (one != node) {
                                other = node;
                                controller.connectNodes(one, other, root,lines);
                                one = null;
                                other = null;
                                break;
                            }
                        }
                    }
                }

            }else {
                // selector for nodes (start from the last node and end at the first)
                for (int i = root.getChildren().size() - 1; i >= 0; i--) {
                    Node node = root.getChildren().get(i);
                    if (node.contains(clickX, clickY)) {
                        System.out.println("yes");
                        if (node instanceof StackPane){
                            node.setOnMouseClicked(e -> {
                                if (event.getClickCount() == 2){
                                    new EntityModify((Entity) node);
                                }
                            });
                        }
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
            if (!controller.isConnectClicked()){
                double deltaX = event.getX() - mouseDownX;
                double deltaY = event.getY() - mouseDownY;
                if (selectedNode != null && selectedNode instanceof Draggable) {
                    ((Draggable) selectedNode).drag(deltaX,deltaY);
                    mouseDownX = event.getX();
                    mouseDownY = event.getY();
                    System.out.println("Node Dragged");
                }
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