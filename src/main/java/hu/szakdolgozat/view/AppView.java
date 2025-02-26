package hu.szakdolgozat.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import hu.szakdolgozat.controller.ApplicationController;
import hu.szakdolgozat.model.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;

/*
* meghatározo kapcsolat duplavonalas rombusz
* kisebb es normalisan mukdodo speializalo kapcsolat
* automazicio erteistes a usernek
* visszatoltes
* nyil megvalositasa az entitas fele
* kulcsjeloles
* */

public class AppView extends Application {
    private final ApplicationController controller = new ApplicationController();
    private Node selectedNode;
    private Node one, other;
    private Node startNode, endNode;
    private double mouseDownX;
    private double mouseDownY;
    private boolean isConnectButtonVisible = false;
    private List<Arrow> arrows = new ArrayList<>();
    private List<Entity> entities;
    private List<Attribute> attributes;
    private List<Relation> relations;
    private List<OwnLine> arrows1;
    private Pane root;

    private static final String LAST_USED_FOLDER = "lastUsedFolder";

    private void saveState(Stage stage) {

        Preferences prefs = Preferences.userNodeForPackage(AppView.class);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save State");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        // Set initial directory to the last used directory
        String lastUsedFolderPath = prefs.get(LAST_USED_FOLDER, null);
        if (lastUsedFolderPath != null) {
            File lastUsedFolder = new File(lastUsedFolderPath);
            if (lastUsedFolder.exists() && lastUsedFolder.isDirectory()) {
                fileChooser.setInitialDirectory(lastUsedFolder);
            }
        }

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                AppState state = new AppState();
                state.setEntityObjects(getEntitiesFromRoot());
                state.setAttributeObjects(getAttributesFromRoot());
                state.setRelationObjects(getRelationsFromRoot());
                state.setConnectionObjects(getLinesFromRoot());
                state.setSpecializerObjects(getSpecializerRelationsFromRoot());

                StateManager.saveState(state, file.getPath());

                // Save the directory of the selected file
                prefs.put(LAST_USED_FOLDER, file.getParentFile().getPath());

                // Display success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Save Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("State saved successfully to " + file.getPath());
                successAlert.showAndWait();
            } catch (IOException e) {
                // Display error alert
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Save Failed");
                errorAlert.setHeaderText("An error occurred while saving the state.");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();

                e.printStackTrace();
            }
        } else {
            // Display warning alert for canceled file selection
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Save Canceled");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("File selection was canceled or failed.");
            warningAlert.showAndWait();
        }
    }

    private List<Entity> getEntitiesFromRoot() {
        List<Entity> entities = new ArrayList<>();
        for (Node node : root.getChildren()) {
            if (node instanceof Entity) {
                entities.add((Entity) node);
            }
        }
        return entities;
    }

    private List<Attribute> getAttributesFromRoot() {
        List<Attribute> attributes = new ArrayList<>();
        for (Node node : root.getChildren()) {
            if (node instanceof Attribute) {
                System.out.println(node);
                attributes.add((Attribute) node);
            }
        }
        return attributes;
    }

    private List<Relation> getRelationsFromRoot() {
        List<Relation> relations = new ArrayList<>();
        for (Node node : root.getChildren()) {
            if (node instanceof Relation) {
                relations.add((Relation) node);
            }
        }
        return relations;
    }

    private List<OwnLine> getLinesFromRoot() {
        List<OwnLine> arrows = new ArrayList<>();
        for (Node node : root.getChildren()) {
            if (node instanceof OwnLine) {
                arrows.add((OwnLine) node);
            }
        }
        return arrows;
    }
    private List<SpecializerRelation> getSpecializerRelationsFromRoot() {
        List<SpecializerRelation> relations = new ArrayList<>();
        for (Node node : root.getChildren()) {
            if (node instanceof SpecializerRelation) {
                relations.add((SpecializerRelation) node);
            }
        }
        return relations;
    }





    @Override
    public void start(Stage stage) {

        // buttons
        Button egyedButton = new Button("Entity");
        Button kapcsolatButton = new Button("Relationship");
        Button attributumButton = new Button("Attribute");
        Button deleteButton = new Button("Delete");
        Button connectButton = new Button("Connect");
        Button specializerButton = new Button("Specializer");
        //Button checkbutton = new Button("Check the arrows");

        //style for the buttons
        egyedButton.getStyleClass().add("action-button");
        kapcsolatButton.getStyleClass().add("action-button");
        attributumButton.getStyleClass().add("action-button");
        connectButton.getStyleClass().add("connect-button");
        deleteButton.getStyleClass().add("delete-button");
        specializerButton.getStyleClass().add("action-button");

        //checkbutton.getStyleClass().add("action-button");

        // Event handlers for buttons
        egyedButton.setOnAction(e -> {
            controller.handleEntityButtonClick();
            controller.toggleButtonStyle(egyedButton, "action-button-clicked");
        });

        kapcsolatButton.setOnAction(e -> {
            controller.handleRelationButtonClick();
            controller.toggleButtonStyle(kapcsolatButton, "action-button-clicked");
        });

        attributumButton.setOnAction(e -> {
            controller.handleAttributeButtonClick();
            controller.toggleButtonStyle(attributumButton, "action-button-clicked");
        });

        specializerButton.setOnAction(e -> {
            controller.handleSpecializeButtonClick();
            controller.toggleButtonStyle(specializerButton, "action-button-clicked");
        });

        connectButton.setOnAction(e -> {
            controller.handleConnectButton();
            controller.toggleButtonStyle(connectButton, "connect-button-clicked");
        });

        // Vbox for the buttons
        VBox buttonPanel = new VBox(10);
        buttonPanel.getChildren().addAll(egyedButton, kapcsolatButton, attributumButton,specializerButton, deleteButton, connectButton);
        buttonPanel.setPadding(new Insets(7));

        //style for the buttonpanel
        buttonPanel.getStyleClass().add("button-panel");

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(buttonPanel);

        root = new Pane();
        borderPane.setCenter(root);
        root.getStyleClass().add("root");

        // MenuBar setup
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        // Add Save and Load menu items
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(e -> saveState(stage));

        MenuItem loadMenuItem = new MenuItem("Load");
        loadMenuItem.setOnAction(e -> loadState(stage));

        MenuItem savePngMenuItem = new MenuItem("Save as PNG");
        savePngMenuItem.setOnAction(e -> saveRootAsPng(stage));

        fileMenu.getItems().addAll(saveMenuItem, loadMenuItem,savePngMenuItem);
        menuBar.getMenus().add(fileMenu);

        borderPane.setTop(menuBar);

        Scene scene = new Scene(borderPane, 700, 500);

        // Load css
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Wrong path given!");
        }

        deleteButton.setOnAction(e -> {
            if (selectedNode != null) {
                List<Node> toRemove = new ArrayList<>();

                for (Node node : root.getChildren()) {
                    if (node instanceof Arrow) {
                        Arrow arrow = (Arrow) node;
                        if (LineChecker.isArrowEndBoundToEntity(arrow, selectedNode)) {
                            System.out.println("Connected arrow found, removing...");
                            toRemove.add(arrow);
                        }
                    }
                }

                // Remove all arrows connected to the selected node
                root.getChildren().removeAll(toRemove);

                // Remove the selected entity itself
                root.getChildren().remove(selectedNode);
                selectedNode = null;

                // Debug output
                System.out.println("Remaining children in root:");
                for (Node node : root.getChildren()) {
                    System.out.println(node);
                }
            }
        });



        root.setOnMousePressed(event -> {
            mouseDownX = event.getX();
            mouseDownY = event.getY();
            double clickX = event.getX();
            double clickY = event.getY();
            System.out.println(clickX + " " + clickY);

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
                double entityWidth = 100;
                double entityHeight = 50;
                Entity entity = new Entity(clickX - entityWidth / 2, clickY - entityHeight / 2, entityWidth, entityHeight,false);
                root.getChildren().add(entity);
                System.out.println("entity positons: " + entity.getPosX() + " " + entity.getPosY());
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
                controller.toggleButtonStyle(egyedButton, "action-button-clicked");
            } else if (controller.isAttributeClicked()) {
                Attribute attribute = new Attribute(clickX , clickY,25,55);
                root.getChildren().add(attribute);
                System.out.println( "attrubite coordinates: " + attribute.getPosX() + " " + attribute.getPosY());
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
                controller.toggleButtonStyle(attributumButton, "action-button-clicked");
            }else if (controller.isRelationshipClicked()) {
                double size = 70;
                double[] points = {
                        0, -size / 2,       // Top point relative to center
                        size / 2, 0,        // Right point
                        0, size / 2,        // Bottom point
                        -size / 2, 0        // Left point
                };


                // Instantiate the Relation at the click location (clickX, clickY)
                Relation diamond = new Relation(clickX, clickY, points);
                root.getChildren().add(diamond);
                diamond.setLayoutX(clickX - size / 2);
                diamond.setLayoutY(clickY - size / 2);
                diamond.setSelected(true);
                selectedNode = diamond;

                // Deselect all other nodes
                for (Node node : root.getChildren()) {
                    if (node instanceof Selectable && node != diamond) {
                        ((Selectable) node).setSelected(false);
                    }
                }
                controller.setRelationshipClicked(false);
                controller.toggleButtonStyle(kapcsolatButton, "action-button-clicked");
            }
            else if (controller.isConnectClicked()) {
                for (Node node : root.getChildren()) {
                    if (!(node instanceof Line)) {
                        if (node.contains(clickX, clickY)) {
                            if (one == null) {
                                one = node;
                                break;
                            } else if (one != node) {
                                other = node;
                                if (one instanceof Entity && other instanceof Entity) {
                                    Alert alert = new Alert(Alert.AlertType.WARNING,"Entity can not be connected to Entity!");
                                    alert.setTitle("Connection failed");
                                    alert.setHeaderText("Invalid Connection");
                                    alert.showAndWait();
                                }else{
                                    String relationType = getConnectionType();
                                    System.out.println(relationType);
                                    boolean arrowAtEnd;
                                    boolean arrowAtStart;
                                    switch (relationType) {
                                        case "1:N":
                                            arrowAtStart = true;
                                            arrowAtEnd = false;
                                            break;
                                        case "N:N":
                                            arrowAtStart = false;
                                            arrowAtEnd = false;
                                            break;
                                        case "1:1":
                                            arrowAtEnd = true;
                                            arrowAtStart = true;
                                            break;
                                        default:
                                            arrowAtEnd = false;
                                            arrowAtStart = false;
                                            break;
                                    }

                                    controller.connectNodes(one, other, root, arrows, arrowAtEnd, arrowAtStart);

                                    Arrow lastLine = arrows.get(arrows.size() - 1);

                                    one = null;
                                    other = null;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (controller.isSpecializeClicked()) {
                SpecializerRelation specializerRelation = new SpecializerRelation(clickX-30 ,clickY-30);
                root.getChildren().add(specializerRelation);
                selectedNode = specializerRelation;
                // Deselect all other nodes
                for (Node node : root.getChildren()) {
                    if (node instanceof Selectable && node != specializerRelation) {
                        ((Selectable) node).setSelected(false);
                    }
                }
                controller.setSpecializeClicked(false);
                controller.toggleButtonStyle(specializerButton, "action-button-clicked");
            } else {
                // selector for nodes (start from the last node and end at the first)
                for (int i = root.getChildren().size() - 1; i >= 0; i--) {
                    Node node = root.getChildren().get(i);
                    if (node.contains(clickX, clickY)) {
                        System.out.println("contains");
                        if (node instanceof StackPane) {
                            node.setOnMouseClicked(e -> {
                                if (event.getClickCount() == 2) {
                                    if (node instanceof Entity) {
                                        new EntityModify((Entity) node);
                                    } else if (node instanceof Attribute) {
                                        new AttributeModify((Attribute) node);
                                    } else if (node instanceof Relation) {
                                        new RelationModify((Relation)node);
                                    }
                                }
                            });
                        }
                        if (node instanceof Selectable) {
                            selectedNode = node;
                            ((Selectable) selectedNode).setSelected(true);
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
                    } else {
                        selectedNode = null;
                        for (Node deselectednodes : root.getChildren()) {
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
            if (!controller.isConnectClicked()) {
                double deltaX = event.getX() - mouseDownX;
                double deltaY = event.getY() - mouseDownY;
                if (selectedNode != null && selectedNode instanceof Draggable) {
                    ((Draggable) selectedNode).drag(deltaX, deltaY);
                    mouseDownX = event.getX();
                    mouseDownY = event.getY();
                }
            }
        });

        // Set the scene to the stage
        stage.setTitle("Ek_editor");
        stage.setScene(scene);
        stage.show();
    }

    private void loadState(Stage stage) {
        Preferences prefs = Preferences.userNodeForPackage(AppView.class);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load State");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        String lastUsedFolderPath = prefs.get(LAST_USED_FOLDER, null);
        if (lastUsedFolderPath != null) {
            File lastUsedFolder = new File(lastUsedFolderPath);
            if (lastUsedFolder.exists() && lastUsedFolder.isDirectory()) {
                fileChooser.setInitialDirectory(lastUsedFolder);
            }
        }
        File file = fileChooser.showOpenDialog(stage);
        try {
            // Load AppState from JSON file
            AppState appState = StateManager.loadState(file.getPath());
            root.getChildren().clear();

            // First, add all non-line nodes
            for (Node node : appState.getAllNodes()) {
                    root.getChildren().add(node);
            }
            for (OwnLine line : appState.getConnectionObjects()) {
                Node startNode = null;
                Node endNode = null;

                // Find the start node
                for (Node node : root.getChildren()) {
                    if (!(node instanceof OwnLine)) {
                        Bounds bounds = node.getBoundsInParent(); // Get visual bounds
                        if (bounds.contains(line.getStartX() - 30, line.getStartY() - 30)) {
                            startNode = node;
                            break; // Found the start node, exit this loop
                        }
                    }
                }

                // Find the end node
                for (Node node : root.getChildren()) {
                    if (!(node instanceof OwnLine)) {
                        Bounds bounds = node.getBoundsInParent(); // Get visual bounds
                        if (bounds.contains(line.getEndX() - 30, line.getEndY() - 30)) {
                            endNode = node;
                            break; // Found the end node, exit this loop
                        }
                    }
                }
                // Bind the identified nodes to the line
                if (startNode != null && endNode != null) {
                    System.out.println("Connecting nodes for line from (" + line.getStartX() + ", " + line.getStartY() + ") to (" + line.getEndX() + ", " + line.getEndY() + ")");
                    line.setStartNodeId(String.valueOf(startNode));
                    line.setEndNodeId(String.valueOf(endNode));
                    controller.connectNodes(startNode, endNode, root, arrows,true,true);
                } else {
                    System.out.println("Failed to find matching nodes for line with coordinates: (" + line.getStartX() + ", " + line.getStartY() + ") to (" + line.getEndX() + ", " + line.getEndY() + ")");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void saveRootAsPng(Stage stage) {
        Preferences prefs = Preferences.userNodeForPackage(AppView.class);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PNG");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        // Set initial directory to the last used directory
        String lastUsedFolderPath = prefs.get(LAST_USED_FOLDER, null);
        if (lastUsedFolderPath != null) {
            File lastUsedFolder = new File(lastUsedFolderPath);
            if (lastUsedFolder.exists() && lastUsedFolder.isDirectory()) {
                fileChooser.setInitialDirectory(lastUsedFolder);
            }
        }

        // Show save dialog
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                // Create a WritableImage to capture the root pane
                WritableImage image = new WritableImage((int) root.getWidth(), (int) root.getHeight());
                SnapshotParameters snapshotParameters = new SnapshotParameters();

                // Take snapshot
                root.snapshot(snapshotParameters, image);

                // Write the image to the selected file

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", file);

                // Display success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Save Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Image saved successfully to " + file.getPath());
                successAlert.showAndWait();
            } catch (IOException e) {
                // Display error alert
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Save Failed");
                errorAlert.setHeaderText("An error occurred while saving the image.");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }
    private String getConnectionType() {
        List<String> choices = Arrays.asList("1:1", "1:N","N:N");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("1:1", choices);
        dialog.setTitle("Choose Connection Type");
        dialog.setHeaderText("Select how the nodes should be connected:");
        dialog.setContentText("Choose:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse("1:1"); // Ha nincs választás, alapértelmezett 1:1 kapcsolat
    }
    private void showWarning(String message, String title, String header) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch();
    }
}

