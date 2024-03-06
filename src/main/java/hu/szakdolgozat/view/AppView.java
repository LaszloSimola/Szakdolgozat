// View
package hu.szakdolgozat.view;

import hu.szakdolgozat.controller.ApplicationController;
import hu.szakdolgozat.model.Attribute;
import hu.szakdolgozat.model.Entity;
import hu.szakdolgozat.model.Relation;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AppView extends Application {
    private final ApplicationController controller = new ApplicationController();
    private Node selectedNode;

    @Override
    public void start(Stage stage) {

        // buttons
        Button egyedButton = new Button("Egyed");
        Button kapcsolatButton = new Button("Kapcsolat");
        Button attributumButton = new Button("Attributum");

        Pane root = new Pane();

        // Event handlers for buttons
        egyedButton.setOnAction(e -> controller.handleEgyedButtonClick());
        kapcsolatButton.setOnAction(e -> controller.handleKapcsolatButtonClick());
        attributumButton.setOnAction(e->controller.handleAttributeButtonClick());

        // Vbox for the buttons
        VBox buttonPanel = new VBox(10);
        buttonPanel.setAlignment(Pos.TOP_LEFT);
        buttonPanel.getChildren().addAll(egyedButton, kapcsolatButton, attributumButton);


        StackPane rootPane = new StackPane();
        rootPane.getChildren().addAll(root, buttonPanel);
        Scene scene = new Scene(rootPane, 700, 500);


        //mouseclick events
        scene.setOnMouseClicked(event -> {
            if (controller.isEntityClicked()) {
                double clickX  = event.getX();
                double clickY  = event.getY();
                Entity entity = new Entity(clickX, clickY , 100, 50);
                entity.setFill(Color.TRANSPARENT); // Set fill color to transparent
                entity.setStroke(Color.BLACK);
                scene.setOnMouseReleased(event1 -> {
                    if ((Math.abs(event.getX() - event1.getX()) < 1) && (Math.abs(event.getY() - event1.getY()) < 1)){
                        root.getChildren().add(entity);
                    }else{
                        return;
                    }
                });
            }
            if (controller.isRelationshipClicked()) {
                Relation polygon = new Relation(
                        event.getX(), event.getY(),
                        event.getX() + 50, event.getY() - 25,
                        event.getX() + 100, event.getY(),
                        event.getX() + 50, event.getY() + 25
                );
                root.getChildren().add(polygon);
            }
            if (controller.isAttributeClicked()) {
                Attribute attribute = new Attribute(event.getX(), event.getY());
                attribute.setCenterX(event.getX());
                attribute.setCenterY(event.getY());
                attribute.setRadiusX(70);
                attribute.setRadiusY(40);
                attribute.setFill(Color.TRANSPARENT); // Set fill color to transparent
                attribute.setStroke(Color.BLACK);
                root.getChildren().add(attribute);
            }
        });
        scene.setOnMousePressed(e -> {
            for (Node node:root.getChildren()) {
                if (node.contains(e.getX(),e.getY())){
                    selectedNode = node;
                    System.out.println("pressed");
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