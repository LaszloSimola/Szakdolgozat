package hu.szakdolgozat.view;

import hu.szakdolgozat.model.Attribute;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class AttributeModify extends Stage {
    private final Attribute attribute;

    public AttributeModify(Attribute attribute) {
        this.attribute = attribute;

        GridPane root = new GridPane();
        root.setVgap(20);
        root.setHgap(30);
        root.setPadding(new Insets(10, 10, 10, 10));

        Text label = new Text("Címke:");
        TextField labelTextField = new TextField(attribute.getTextNode().getText()); // Set the initial text

        Text widthText = new Text("Szélesség:");
        Spinner<Integer> widthSpinner = new Spinner<>(1, 500, (int) (attribute.getEllipse().getRadiusX() * 2));
        widthSpinner.setEditable(true);

        Text heightText = new Text("Magasság:");
        Spinner<Integer> heightSpinner = new Spinner<>(1, 500, (int) (attribute.getEllipse().getRadiusY() * 2));
        heightSpinner.setEditable(true);

        Text colorText = new Text("Szín:");
        ColorPicker colorPicker = new ColorPicker(attribute.getStrokeColor());

        Text strokeWidthText = new Text("Vonalvastagság:");
        Spinner<Integer> strokeWidthSpinner = new Spinner<>(1, 10, (int) attribute.getEllipse().getStrokeWidth());
        strokeWidthSpinner.setEditable(true);

        root.addColumn(0, label, widthText, heightText, colorText, strokeWidthText);
        root.addColumn(1, labelTextField, widthSpinner, heightSpinner, colorPicker, strokeWidthSpinner);

        Button okBtn = new Button("ok");
        Button cancelBtn = new Button("megse");

        root.addRow(5, okBtn, cancelBtn);

        cancelBtn.setOnAction(e -> close());

        okBtn.setOnAction(e -> {
            attribute.setTextNode(labelTextField.getText());
            attribute.getEllipse().setRadiusX(widthSpinner.getValue() / 2.0);
            attribute.getEllipse().setRadiusY(heightSpinner.getValue() / 2.0);
            attribute.setStrokeColor(colorPicker.getValue());
            attribute.setStrokeWidth(strokeWidthSpinner.getValue());
            close();
        });

        Scene scene = new Scene(root);
        this.setScene(scene);
        show();
    }
}
