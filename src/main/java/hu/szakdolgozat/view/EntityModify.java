package hu.szakdolgozat.view;

import hu.szakdolgozat.model.Entity;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EntityModify extends Stage {
    private final Entity entity;

    public EntityModify(Entity entity){
        this.entity = entity;

        GridPane root = new GridPane();
        root.setVgap(20);
        root.setHgap(30);
        root.setPadding(new Insets(10, 10, 10, 10));

        Text label = new Text("Label:");
        TextField labelTextfield = new TextField(entity.getTextNode().getText()); // Set the initial text

        Text szelesseg = new Text("Width:");
        Spinner<Integer> szelessegSpinner = new Spinner<>(1, 500, (int) entity.getRectangle().getWidth());
        szelessegSpinner.setEditable(true);

        Text magassag = new Text("Height:");
        Spinner<Integer> magassagSpinner = new Spinner<>(1, 500, (int) entity.getRectangle().getHeight());
        magassagSpinner.setEditable(true);

        Text szinText = new Text("Color:");
        ColorPicker colorPicker = new ColorPicker(entity.getStrokeColor());

        Text vastagsag = new Text("Line Width:");
        Spinner<Integer> vastagsagspinner = new Spinner<>(1, 10, (int) entity.getStrokeWidth()); // Updated to use entity's strokeWidth
        vastagsagspinner.setEditable(true);

        Text gyengeEgyedText = new Text("Weak Entity:");
        CheckBox gyengeEgyedCheckbox = new CheckBox();
        gyengeEgyedCheckbox.setSelected(entity.isWeakEntity());

        root.addColumn(0, label, szelesseg, magassag, szinText, vastagsag, gyengeEgyedText);
        root.addColumn(1, labelTextfield, szelessegSpinner, magassagSpinner, colorPicker, vastagsagspinner, gyengeEgyedCheckbox);

        Button okBtn = new Button("ok");
        Button megseBtn = new Button("Cancel");

        root.addRow(6, okBtn, megseBtn);

        megseBtn.setOnAction(e -> {
            close();
        });

        okBtn.setOnAction(e -> {
            entity.setTextNode(labelTextfield.getText());
            entity.updateSize(szelessegSpinner.getValue(), magassagSpinner.getValue());
            entity.setStrokeColor(colorPicker.getValue());
            entity.setStrokeWidth(vastagsagspinner.getValue()); // Updated to use setter method
            entity.setWeakEntity(gyengeEgyedCheckbox.isSelected());
            close();
        });

        this.setTitle("Entity Modify");
        Scene scene = new Scene(root);
        this.setScene(scene);
        show();
    }
}
