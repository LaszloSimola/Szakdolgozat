package hu.szakdolgozat.view;

import hu.szakdolgozat.controller.ApplicationController;
import hu.szakdolgozat.model.*;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.BoundingBox;
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
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;

public class AppView extends Application {
    private final ApplicationController controller = new ApplicationController();
    private Node selectedNode;
    private Node one, other;
    private double mouseDownX;
    private double mouseDownY;
    private List<Arrow> arrows = new ArrayList<>();
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
                state.setConnectionObjects(getArrowsFromRoot());
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

    private List<Arrow> getArrowsFromRoot() {
        List<Arrow> arrows = new ArrayList<>();
        for (Node node : root.getChildren()) {
            if (node instanceof Arrow) {
                arrows.add((Arrow) node);
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
        Button entityButton = new Button("Entity");
        Button relationButton = new Button("Relationship");
        Button attributeButton = new Button("Attribute");
        Button deleteButton = new Button("Delete");
        Button connectButton = new Button("Connect");
        Button specializeButton = new Button("Specializer");

        //style for the buttons
        entityButton.getStyleClass().add("action-button");
        relationButton.getStyleClass().add("action-button");
        attributeButton.getStyleClass().add("action-button");
        connectButton.getStyleClass().add("connect-button");
        deleteButton.getStyleClass().add("delete-button");
        specializeButton.getStyleClass().add("action-button");

        connectButton.setOnAction(event -> {
            controller.handleToggleMode(connectButton, "connect-button-clicked");
        });

        entityButton.setOnAction(event -> {
            controller.handleToggleMode(entityButton, "entity-button-clicked");
        });

        relationButton.setOnAction(event -> {
            controller.handleToggleMode(relationButton, "relationship-button-clicked");
        });

        attributeButton.setOnAction(event -> {
            controller.handleToggleMode(attributeButton, "attribute-button-clicked");
        });

        specializeButton.setOnAction(event -> {
            controller.handleToggleMode(specializeButton, "specialize-button-clicked");
        });


        // Vbox for the buttons
        VBox buttonPanel = new VBox(10);
        buttonPanel.getChildren().addAll(entityButton, relationButton, attributeButton, specializeButton, deleteButton, connectButton);
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

        fileMenu.getItems().addAll(saveMenuItem, loadMenuItem, savePngMenuItem);
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
                Entity entity = new Entity(clickX - entityWidth / 2, clickY - entityHeight / 2, entityWidth, entityHeight, false);
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
                controller.toggleButtonStyle(entityButton, "entity-button-clicked");
            } else if (controller.isAttributeClicked()) {
                Attribute attribute = new Attribute(clickX, clickY, 25, 55);
                root.getChildren().add(attribute);
                System.out.println("attrubite coordinates: " + attribute.getPosX() + " " + attribute.getPosY());
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
                controller.toggleButtonStyle(attributeButton, "attribute-button-clicked");
            } else if (controller.isRelationshipClicked()) {
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
                controller.toggleButtonStyle(relationButton, "relationship-button-clicked");
            } else if (controller.isConnectClicked()) {
                for (Node node : root.getChildren()) {
                    if (!(node instanceof Line)) {
                        if (node.contains(clickX, clickY)) {
                            if (one == null) {
                                one = node;
                                break;
                            } else if (one != node) {
                                other = node;
                                if (controller.areNodesConnected(one, other,arrows)) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Connection Exists");
                                    alert.setHeaderText("Nodes Already Connected");
                                    alert.setContentText("These nodes are already connected.");
                                    alert.showAndWait();
                                    one = null;
                                    other = null;
                                    break;
                                }
                                if (one instanceof Entity && other instanceof Entity) {
                                    Alert alert = new Alert(Alert.AlertType.WARNING, "Entity can not be connected to Entity!");
                                    alert.setTitle("Connection failed");
                                    alert.setHeaderText("Invalid Connection");
                                    alert.showAndWait();
                                } else if(one instanceof Relation && other instanceof Entity || one instanceof Entity && other instanceof Relation) {
                                    String relationType = getConnectionType();
                                    boolean arrowAtEnd;
                                    boolean arrowAtStart;
                                    switch (relationType) {
                                        case "Arrow":
                                            if (one instanceof Entity && other instanceof Relation) {
                                                arrowAtStart = true;
                                                arrowAtEnd = false;
                                            }else{
                                                arrowAtStart = false;
                                                arrowAtEnd = true;
                                            }
                                            break;
                                        case "Line":
                                            arrowAtStart = false;
                                            arrowAtEnd = false;
                                            break;
                                        default:
                                            arrowAtEnd = false;
                                            arrowAtStart = false;
                                            break;
                                    }
                                    if (!relationType.isEmpty()){
                                        controller.connectNodes(one, other, root, arrows, arrowAtEnd, arrowAtStart);
                                    }

                                    one = null;
                                    other = null;
                                    break;
                                } else{
                                    controller.connectNodes(one, other, root, arrows, false, false);

                                    one = null;
                                    other = null;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (controller.isSpecializeClicked()) {
                SpecializerRelation specializerRelation = new SpecializerRelation(clickX - 30, clickY - 30);
                root.getChildren().add(specializerRelation);
                selectedNode = specializerRelation;
                // Deselect all other nodes
                for (Node node : root.getChildren()) {
                    if (node instanceof Selectable && node != specializerRelation) {
                        ((Selectable) node).setSelected(false);
                    }
                }
                controller.setSpecializeClicked(false);
                controller.toggleButtonStyle(specializeButton, "specialize-button-clicked");
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
                                        new RelationModify((Relation) node);
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
        if (file == null) return; // User canceled

        try {
            // Load AppState from JSON file
            AppState appState = StateManager.loadState(file.getPath());
            root.getChildren().clear();

            // First, add all non-arrow nodes
            for (Node node : appState.getAllNodes()) {
                root.getChildren().add(node);
            }

            // Now, process the arrows
            for (Arrow arrow : appState.getConnectionObjects()) {
                Node startNode = findClosestNode(arrow.getStartX(), arrow.getStartY());
                Node endNode = findClosestNode(arrow.getEndX(), arrow.getEndY());

                if (startNode != null && endNode != null) {
                    System.out.println("✅ Connecting nodes for restored arrow from ("
                            + arrow.getStartX() + ", " + arrow.getStartY() + ") to ("
                            + arrow.getEndX() + ", " + arrow.getEndY() + ")");

                    // Ensure arrow properties are restored
                    boolean isArrowAtStartVisible = arrow.toDTO().isArrowAtStartVisible();
                    boolean isArrowAtEndVisible = arrow.toDTO().isArrowAtEndVisible();

                    // Call connectNodes (this will automatically add the arrow)
                    controller.connectNodes(startNode, endNode, root, arrows, isArrowAtEndVisible, isArrowAtStartVisible);
                } else {
                    System.out.println("⚠️ Warning: Could not find valid start or end nodes for arrow at ("
                            + arrow.getStartX() + ", " + arrow.getStartY() + ") to ("
                            + arrow.getEndX() + ", " + arrow.getEndY() + ")");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Node findClosestNode(double x, double y) {
        Node closestNode = null;
        double minDistance = Double.MAX_VALUE;
        final double BASE_SEARCH_RADIUS = 30.0; // Start with 30 pixels

        for (Node node : root.getChildren()) {
            if (node instanceof Arrow) continue; // Skip arrows

            // Get node bounds in parent coordinates
            Bounds bounds = node.getBoundsInParent();
            double centerX = bounds.getMinX() + bounds.getWidth() / 2;
            double centerY = bounds.getMinY() + bounds.getHeight() / 2;

            // Adjust search radius dynamically based on node size
            double searchRadius = Math.max(BASE_SEARCH_RADIUS, Math.max(bounds.getWidth(), bounds.getHeight()) * 0.5);

            // Create an expanded bounding box
            Bounds expandedBounds = new BoundingBox(
                    bounds.getMinX() - searchRadius,
                    bounds.getMinY() - searchRadius,
                    bounds.getWidth() + 2 * searchRadius,
                    bounds.getHeight() + 2 * searchRadius
            );

            // Check if (x, y) is within the expanded bounds
            if (expandedBounds.contains(x, y)) {
                double distance = Math.hypot(centerX - x, centerY - y);
                if (distance < minDistance) {
                    closestNode = node;
                    minDistance = distance;
                }
            }
        }

        if (closestNode == null) {
            System.out.println("⚠️ No node found near (" + x + ", " + y + ") -- Expanding search radius!");
            // Try again with a larger search radius
            return retryFindClosestNode(x, y, BASE_SEARCH_RADIUS * 2);
        } else {
            System.out.println("✅ Closest node found at (" + closestNode.getLayoutX() + ", " + closestNode.getLayoutY() + ")");
        }

        return closestNode;
    }

    private Node retryFindClosestNode(double x, double y, double searchRadius) {
        Node closestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (Node node : root.getChildren()) {
            if (node instanceof Arrow) continue; // Skip arrows

            Bounds bounds = node.getBoundsInParent();
            double centerX = bounds.getMinX() + bounds.getWidth() / 2;
            double centerY = bounds.getMinY() + bounds.getHeight() / 2;

            Bounds expandedBounds = new BoundingBox(
                    bounds.getMinX() - searchRadius,
                    bounds.getMinY() - searchRadius,
                    bounds.getWidth() + 2 * searchRadius,
                    bounds.getHeight() + 2 * searchRadius
            );

            if (expandedBounds.contains(x, y)) {
                double distance = Math.hypot(centerX - x, centerY - y);
                if (distance < minDistance) {
                    closestNode = node;
                    minDistance = distance;
                }
            }
        }

        if (closestNode == null) {
            System.out.println("❌ No node found even after retry with " + searchRadius + "px radius.");
        } else {
            System.out.println("🔄 Retry success! Closest node found at (" + closestNode.getLayoutX() + ", " + closestNode.getLayoutY() + ")");
        }

        return closestNode;
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
                // Save the old style
                String oldStyle = root.getStyle();

                // Set plain white background
                root.setStyle("-fx-background-color: white;");


                WritableImage image = new WritableImage((int) root.getWidth(), (int) root.getHeight());
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                // Take snapshot with white background
                root.snapshot(snapshotParameters, image);

                // Restore the old style
                root.setStyle(oldStyle);

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", file);

                // Display success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Save Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Image saved successfully to " + file.getPath());
                successAlert.showAndWait();
            } catch (IOException e) {
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
        // Updated choices to only include "Arrow" and "Line"
        List<String> choices = Arrays.asList("Arrow", "Line");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Line", choices); // Default is "Line"
        dialog.setTitle("Choose Connection Type");
        dialog.setHeaderText("Select how the nodes should be connected:");
        dialog.setContentText("(The arrow will face in the order of the click)");

        Optional<String> result = dialog.showAndWait();

        // Return null if the user cancels, otherwise return the selected value
        return result.orElse("");
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

