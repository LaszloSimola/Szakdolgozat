package hu.szakdolgozat.view;

import hu.szakdolgozat.model.Relation;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RelationModify extends Stage {
    private final Relation relation;

    public RelationModify(Relation relation) {
        this.relation = relation;

        GridPane root = new GridPane();
        root.setVgap(20);
        root.setHgap(30);
        root.setPadding(new Insets(10, 10, 10, 10));

        Text labelText = new Text("Label:");
        TextField labelTextfield = new TextField(relation.getTextNode().getText());

        Text colorText = new Text("Stroke Color:");
        ColorPicker colorPicker = new ColorPicker(relation.getStrokeColor());

        Text posXText = new Text("Position X:");
        Spinner<Double> posXSpinner = new Spinner<>(0.0, 1000.0, relation.getPosX());
        posXSpinner.setEditable(true);

        Text posYText = new Text("Position Y:");
        Spinner<Double> posYSpinner = new Spinner<>(0.0, 1000.0, relation.getPosY());
        posYSpinner.setEditable(true);

        Text sizeText = new Text("Size:");
        Spinner<Double> sizeSpinner = new Spinner<>(0.1, 10.0, relation.getSize());
        sizeSpinner.setEditable(true);

        Text thicknessText = new Text("Line Thickness:");
        Spinner<Double> thicknessSpinner = new Spinner<>(1.0, 10.0, relation.getStrokeWidth());
        thicknessSpinner.setEditable(true);

        root.addColumn(0, labelText, colorText, posXText, posYText, sizeText, thicknessText);
        root.addColumn(1, labelTextfield, colorPicker, posXSpinner, posYSpinner, sizeSpinner, thicknessSpinner);

        Button okBtn = new Button("OK");
        Button cancelBtn = new Button("Cancel");

        root.addRow(6, okBtn, cancelBtn);

        cancelBtn.setOnAction(e -> {
            close();
        });

        okBtn.setOnAction(e -> {
            relation.setTextNode(labelTextfield.getText());
            relation.setStrokeColor(colorPicker.getValue());
            relation.setPosX(posXSpinner.getValue());
            relation.setPosY(posYSpinner.getValue());
            relation.setSize(sizeSpinner.getValue());
            relation.setStrokeWidth(thicknessSpinner.getValue());
            close();
        });

        Scene scene = new Scene(root);
        this.setScene(scene);
        show();
    }
}
