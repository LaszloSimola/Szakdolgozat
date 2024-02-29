// View
package hu.szakdolgozat.view;

import hu.szakdolgozat.controller.ApplicationController;
import hu.szakdolgozat.model.Entity;
import hu.szakdolgozat.model.Relation;
import javafx.application.Application;
import javafx.geometry.Pos;
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

    @Override
    public void start(Stage stage) {
        // Create buttons
        Button egyedButton = new Button("Egyed");
        Button kapcsolatButton = new Button("Kapcsolat");
        Button attributumButton = new Button("Attributum");

        Pane root = new Pane();

        // Event handlers for buttons
        egyedButton.setOnAction(e -> controller.handleEgyedButtonClick());
        kapcsolatButton.setOnAction(e -> controller.handleKapcsolatButtonClick());

        // Create a VBox layout container for buttons
        VBox buttonPanel = new VBox(10); // 10 is spacing between buttons
        buttonPanel.setAlignment(Pos.TOP_LEFT); // Align buttons to the top left
        buttonPanel.getChildren().addAll(egyedButton, kapcsolatButton, attributumButton); // Add buttons to the VBox

        // Set the scene with buttonPanel and root as root and specify its size
        StackPane rootPane = new StackPane();

        rootPane.getChildren().addAll(root, buttonPanel);
        Scene scene = new Scene(rootPane, 500, 300);

        scene.setOnMouseClicked(event -> {
                if (controller.isEntityClicked()){
                    Entity entity = new Entity(event.getX(), event.getY(), 100, 50);
                    entity.setFill(Color.TRANSPARENT); // Set fill color to transparent
                    entity.setStroke(Color.BLACK);
                    root.getChildren().add(entity);
                }
                if (controller.isRelationshipClicked()){
                    Relation polygon = new Relation(
                            event.getX(), event.getY(),
                            event.getX() + 50, event.getY() - 25,
                            event.getX() + 100, event.getY(),
                            event.getX() + 50, event.getY() + 25
                    );
                    root.getChildren().add(polygon);
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