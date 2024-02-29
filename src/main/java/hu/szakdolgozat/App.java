package hu.szakdolgozat;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    private boolean egyedclicked = false;
    private boolean kapcsolatClicked = false;
    private boolean AttrClicked = false;

    @Override
    public void start(Stage stage) {
        // Create buttons
        Button egyedButton = new Button("Egyed");
        Button kapcsolatButton = new Button("Kapcsolat");
        Button attributumButton = new Button("Attributum");

        Pane root = new Pane();

        // Event handler for Egyed button
        egyedButton.setOnAction(e -> {
            if (!egyedclicked){
                egyedclicked = true;
            }else{
                egyedclicked = false;
            }
        });
        kapcsolatButton.setOnAction(e -> {
            egyedclicked = false;
            if (!kapcsolatClicked){
                kapcsolatClicked = true;
            }else{
                kapcsolatClicked = false;
            }
        });
        // Create a VBox layout container for buttons
        VBox buttonPanel = new VBox(10); // 10 is spacing between buttons
        buttonPanel.setAlignment(Pos.TOP_LEFT); // Align buttons to the top left
        buttonPanel.getChildren().addAll(egyedButton, kapcsolatButton, attributumButton); // Add buttons to the VBox

        // Set the scene with buttonPanel and root as root and specify its size
        StackPane rootPane = new StackPane();

        rootPane.getChildren().addAll(root, buttonPanel);
        Scene scene = new Scene(rootPane, 500, 300);

        // Event handler for mouse click to draw rectangles
        scene.setOnMouseClicked(event -> {
            if (egyedclicked){
                Rectangle rectangle = new Rectangle(event.getX(), event.getY(), 100, 50);
                rectangle.setFill(Color.TRANSPARENT); // Set fill color to transparent
                rectangle.setStroke(Color.BLACK);
                root.getChildren().add(rectangle);
            }
            if (kapcsolatClicked){
                Polygon diamond = new Polygon();
                root.getChildren().add(diamond);
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
