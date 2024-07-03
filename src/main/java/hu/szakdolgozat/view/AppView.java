package hu.szakdolgozat.view;

import hu.szakdolgozat.controller.ApplicationController;
import hu.szakdolgozat.model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;



public class AppView extends Application {
    private final ApplicationController controller = new ApplicationController();
    private Node selectedNode;
    private Node one, other;
    private double mouseDownX;
    private double mouseDownY;
    private boolean isConnectButtonVisible = false;
    List<OwnLine> lines = new ArrayList<>();
    private List<Entity> entities;
    private List<Attribute> attributes;
    private List<Relation> relations;
    private List<OwnLine> lines1;
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
                state.setEntities(getEntitiesFromRoot());
                state.setAttributes(getAttributesFromRoot());
                state.setRelations(getRelationsFromRoot());
                state.setConnections(getLinesFromRoot());

                StateManager.saveState(state, file.getPath());

                System.out.println(state.getAttributes() + " entities: " + state.getEntityObjects());

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
        List<OwnLine> lines = new ArrayList<>();
        for (Node node : root.getChildren()) {
            if (node instanceof OwnLine) {
                lines.add((OwnLine) node);
            }
        }
        return lines;
    }

    private void restoreStateToRoot(AppState state) {
        root.getChildren().clear();
        root.getChildren().addAll(state.getEntityObjects());
        root.getChildren().addAll(state.getAttributeObjects());
        root.getChildren().addAll(state.getRelationObjects());
        root.getChildren().addAll(state.getConnectionObjects());
    }




    @Override
    public void start(Stage stage) {

        // buttons
        Button egyedButton = new Button("Egyed");
        Button kapcsolatButton = new Button("Kapcsolat");
        Button attributumButton = new Button("Attributum");
        Button deleteButton = new Button("Delete");
        Button connectButton = new Button("Connect");
        //Button checkbutton = new Button("Check the lines");

        //style for the buttons
        egyedButton.getStyleClass().add("action-button");
        kapcsolatButton.getStyleClass().add("action-button");
        attributumButton.getStyleClass().add("action-button");
        connectButton.getStyleClass().add("connect-button");
        deleteButton.getStyleClass().add("delete-button");
        //checkbutton.getStyleClass().add("action-button");

        // Event handlers for buttons
        egyedButton.setOnAction(e -> controller.handleEntityButtonClick());
        kapcsolatButton.setOnAction(e -> controller.handleRelationButtonClick());
        attributumButton.setOnAction(e -> controller.handleAttributeButtonClick());
        connectButton.setOnAction(e -> {
            controller.handleConnectButton();
            if (controller.isConnectClicked()) {
                connectButton.getStyleClass().add("connect-button-clicked");
            } else {
                connectButton.getStyleClass().remove("connect-button-clicked");
            }
            isConnectButtonVisible = !isConnectButtonVisible;
        });

        // Vbox for the buttons
        VBox buttonPanel = new VBox(10);
        buttonPanel.getChildren().addAll(egyedButton, kapcsolatButton, attributumButton, deleteButton, connectButton);
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

        fileMenu.getItems().addAll(saveMenuItem, loadMenuItem);
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
            boolean bounded = false;
            if (selectedNode != null) {
                if (selectedNode instanceof Line) {
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

                    // if line is not connected to a node then it should be deleted, after an entity was deleted
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
                double entityWidth = 100;
                double entityHeight = 50;
                Entity entity = new Entity(clickX - entityWidth / 2, clickY - entityHeight / 2, entityWidth, entityHeight);
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
            } else if (controller.isAttributeClicked()) {
                double attributeWidth = 115;
                double attributeHeight = 50;
                Attribute attribute = new Attribute(clickX - attributeWidth / 2, clickY - attributeHeight / 2, attributeHeight, attributeWidth);
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
            }else if (controller.isRelationshipClicked()) {
                double size = 70;
                double[] points = {
                        clickX, clickY - size / 2,
                        clickX + size / 2, clickY,
                        clickX, clickY + size / 2,
                        clickX - size / 2, clickY
                };
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
            } else if (controller.isConnectClicked()) {
                for (Node node : root.getChildren()) {
                    if (!(node instanceof Line)) {
                        if (node.contains(clickX, clickY)) {
                            if (one == null) {
                                one = node;
                                break;
                            } else if (one != node) {
                                other = node;
                                controller.connectNodes(one, other, root, lines);
                                // a kapcsolodok hozzárendelése aa vonalhoz
                                for (int i = lines.size() - 1 ; i > 0 ; i--) {
                                    lines.get(i).setStartNode(one);
                                    lines.get(i).setEndNode(other);
                                    System.out.println(lines.get(i).getStartNode() + " ,asoidk " + lines.get(i).getEndNode());
                                }
                                one = null;
                                other = null;
                                break;
                            }
                        }
                    }
                }
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
                    System.out.println("Node Dragged");
                }
            }
        });

        // Set the scene to the stage
        stage.setTitle("Ek_editor");
        stage.setScene(scene);
        stage.show();
    }

    private void loadState(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load State");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                AppState state = hu.szakdolgozat.model.StateManager.loadState(file.getPath());
                restoreStateToRoot(state);
                System.out.println("State loaded from " + file.getPath());
            } catch (IOException e) {
                System.out.println("Error loading state: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("File selection was canceled or failed.");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

