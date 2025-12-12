package org.uav.prezentui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.PopOver;
import java.util.*;

import static javafx.geometry.Pos.*;

// Clasa care controleaza elementele (butoane, meniuri etc) aplicatiei
public class HelloController {
    // ID-uri pentru fiecare button
    @FXML private Button btnIntPrez;
    @FXML private Button btnVizPrez;
    @FXML private Button btnVizAdminPrez;

    // Pop-over pentru introducere prezente
    @FXML protected void onHelloButtonClick() {
        // creeam un layout vertical pentru a ordona frumos elementele
        VBox box = new VBox(10);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(5);

        // creeam combo box-ul pentru materii
        ComboBox<String> cmb = new ComboBox<String>();
        cmb.getItems().addAll(
                "Ecuatii diferentiale",
                "Baze de date",
                "Programare orientata obiect",
                "Algoritmica grafurilor"
        );

        // creeam un grup de butoane radio (o singura alegere) pentru materii
        ToggleGroup group = new ToggleGroup();
        List<String> labels = List.of("Curs", "Laborator");
        List<RadioButton> buttons = new ArrayList<>();
        for (String label : labels) {
            RadioButton rb = new RadioButton(label);
            rb.setToggleGroup(group);
            buttons.add(rb);
        }

        // adaugam elemente ca "noduri"
        box.getChildren().addAll(
                new Label("Nume:"),
                new TextField(),
                new Separator(),
                new Label("Materie:"),
                cmb,
                new Separator()
        );

        // nu putem imbina liste de noduri cu noduri pe cont propriu, deci le separam
        box.getChildren().addAll(buttons);

        // creeam un layout orizontal inauntrul layout-ului nostru pentru a aseza butonul pe partea dreapta a
        // ferestrei si adaugam butonul
        HBox btnPrezentBox = new HBox();
        btnPrezentBox.setAlignment(CENTER_RIGHT);

        // adaugam un button in layout-ul orizontal
        btnPrezentBox.getChildren().add(new Button("Prezent!"));

        // adaugam HBox-ul nostru in layout-ul principal
        box.getChildren().addAll(
                new Separator(),
                btnPrezentBox
        );

        // creeam Pop-overul cu continutul layout-ului "box" si il afisam ca copil/sub-nod al lui "btnIntPrez"
        PopOver popOver = new PopOver(box);
        popOver.setTitle("Introducere prezenta"); //setam un titlu in caz ca popover-ul este detasat (tras cu mouse-ul)
        popOver.show(btnIntPrez);
    }

    // Pop-over pentru vizualizare prezente (admin)
    @FXML protected void onVizPrezAdminClick() {
        // creeam un layout vertical
        VBox box = new VBox(10);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(5);

        // creeam un layout orizontal inauntrul layout-ului nostru pentru a aseza butonul pe partea dreapta a
        // ferestrei si adaugam butonul
        HBox btnPrezentBox = new HBox();
        btnPrezentBox.setAlignment(CENTER_RIGHT);

        // adaugam un button in layout-ul orizontal
        btnPrezentBox.getChildren().add(
                new Button("Submite si intra")
        );

        // adaugam elemente in layout
        box.getChildren().addAll(
                new Label("ATENTIE! Aceaste portiune este menita doar pentru profesori si administratori!"),
                new Separator(),
                new Label("Parola:"),
                new PasswordField(),
                btnPrezentBox
        );

        // creeam Pop-overul cu continutul layout-ului "box" si il afisam ca copil/sub-nod al lui "btnIntPrez"
        PopOver popOver = new PopOver(box);
        popOver.setTitle("Autentificare"); //setam un titlu in caz ca popover-ul este detasat (tras cu mouse-ul)
        popOver.show(btnVizAdminPrez);
    }
}
