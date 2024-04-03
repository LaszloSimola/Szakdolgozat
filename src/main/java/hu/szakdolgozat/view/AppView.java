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
        buttonPanel.setPadding(new Insets(7));

        //style for the buttonpanel
        buttonPanel.getStyleClass().add("button-panel");

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(buttonPanel);

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

        root.setOnMousePressed(event -> {
            mouseDownX = event.getX();
            mouseDownY = event.getY();
            double clickX = event.getX();
            double clickY = event.getY();
            if (controller.isEntityClicked()){
                // Create a new entity
                Entity entity = new Entity(clickX, clickY);
                root.getChildren().add(entity);
                entity.setSelected(true);
                selectedNode = entity;

                // Deselect all other entities
                for (Node node : root.getChildren()) {
                    if (node instanceof Entity && node != entity) {
                        ((Entity) node).setSelected(false);
                        System.out.println("deselected");
                    }
                }
               controller.setEntityClicked(false);
            }else {
                // selector for entities
                for (Node node : root.getChildren()) {
                    if (node.contains(clickX, clickY)) {
                        selectedNode = node;
                        ((Entity) selectedNode).setSelected(true);
                        System.out.println("Entity pressed.");

                        //deselect others at the same time
                        for (Node n : root.getChildren()) {
                            if (n instanceof Entity && n != selectedNode) {
                                ((Entity) n).setSelected(false);
                                System.out.println("others deselected");
                            }
                        }
                        break;

                    }else{
                        for (Node n : root.getChildren()) {
                            if (n instanceof Entity && !n.contains(clickX,clickY)) {
                                ((Entity) n).setSelected(false);
                                selectedNode = null;
                                System.out.println("others deselected");
                            }
                        }
                        System.out.println("else");

                    }
                }

            }
        });

        root.setOnMouseDragged(event -> {
            // If an entity is selected, drag it
            if (selectedNode != null && selectedNode instanceof Entity) {
                double deltaX = event.getX() - mouseDownX;
                double deltaY = event.getY() - mouseDownY;
                ((Entity) selectedNode).setX(((Entity) selectedNode).getX() + deltaX);
                ((Entity) selectedNode).setY(((Entity) selectedNode).getY() + deltaY);
                mouseDownX = event.getX();
                mouseDownY = event.getY();
                System.out.println("Dragged");
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
    public void deselector(Pane root){
        for (Node n : root.getChildren()) {
            if (n instanceof Entity && n != selectedNode) {
                ((Entity) n).setSelected(false);
                System.out.println("others deselected");
            }
        }
    }



    public static void main(String[] args) {
        launch();
    }

}