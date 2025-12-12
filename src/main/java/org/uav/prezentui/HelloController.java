package org.uav.prezentui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML private Button btnIntPrez;
    @FXML private Button btnVizPrez;
    @FXML private Button btnVizAdminPrez;
    @FXML protected void onHelloButtonClick() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(5);

        ComboBox<String> cmb = new ComboBox<String>();
        cmb.getItems().addAll(
                "Ecuatii diferentiale",
                "Baze de date",
                "Programare orientata obiect",
                "Algoritmica grafurilor"
        );

        ToggleGroup group = new ToggleGroup();
        List<String> labels = List.of("Curs", "Laborator");
        List<RadioButton> buttons = new ArrayList<>();
        for (String label : labels) {
            RadioButton rb = new RadioButton(label);
            rb.setToggleGroup(group);
            buttons.add(rb);
        }

        box.getChildren().addAll(
                new Label("Nume:"),
                new TextField(),
                new Label("Materie:"),
                cmb
        );

        box.getChildren().addAll(buttons);

        box.getChildren().add(
            new Button("Prezent!")
        );

        PopOver popOver = new PopOver(box);
        popOver.show(btnIntPrez);
    }

    @FXML protected void onVizPrezAdminClick() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(5);

        box.getChildren().addAll(
                new Label("ATENTIE! Aceaste portiune este menita doar pentru profesori si administratori!"),
                new Label("Parola:"),
                new PasswordField(),
                new Button("Submite si intra")
        );

        PopOver popOver = new PopOver(box);
        popOver.show(btnVizAdminPrez);
    }
}
