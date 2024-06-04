package hu.szakdolgozat.view;

import hu.szakdolgozat.model.Entity;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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

        Text label = new Text("Címke:");
        TextField labelTextfield = new TextField(entity.getTextNode().getText()); // Set the initial text

        Text szelesseg = new Text("Szélesség:");
        Spinner<Integer> szelessegSpinner = new Spinner<>(1, 500, (int) entity.getRectangle().getWidth());
        szelessegSpinner.setEditable(true);

        Text magassag = new Text("Magasság:");
        Spinner<Integer> magassagSpinner = new Spinner<>(1, 500, (int) entity.getRectangle().getHeight());
        magassagSpinner.setEditable(true);

        Text szinText = new Text("Szín:");
        ColorPicker colorPicker = new ColorPicker(entity.getStrokeColor());

        Text vastagsag = new Text("Vonalvastagság:");
        Spinner<Integer> vastagsagspinner = new Spinner<>(1, 10, (int) entity.getRectangle().getStrokeWidth());
        vastagsagspinner.setEditable(true);

        Text gyengeEgyedText = new Text("Gyenge egyed:");
        CheckBox gyengeEgyedCheckbox = new CheckBox();

        root.addColumn(0, label, szelesseg, magassag, szinText, vastagsag, gyengeEgyedText);
        root.addColumn(1, labelTextfield, szelessegSpinner, magassagSpinner, colorPicker, vastagsagspinner, gyengeEgyedCheckbox);

        Button okBtn = new Button("ok");
        Button megseBtn = new Button("megse");

        root.addRow(6, okBtn, megseBtn);

        megseBtn.setOnAction(e -> {
            close();
        });

        okBtn.setOnAction(e -> {
            entity.setTextNode(labelTextfield.getText());
            entity.getRectangle().setWidth(szelessegSpinner.getValue());
            entity.getRectangle().setHeight(magassagSpinner.getValue());
            entity.setStrokeColor(colorPicker.getValue());
            entity.getRectangle().setStrokeWidth(vastagsagspinner.getValue());
            close();
        });

        Scene scene = new Scene(root);
        this.setScene(scene);
        show();
    }
}
