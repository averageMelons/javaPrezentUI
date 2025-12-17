package org.uav.prezentui;

import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;

import static javafx.geometry.Pos.*;

// Clasa care controleaza elementele (butoane, meniuri etc) aplicatiei
public class PrezentUIController {
    // ID-uri pentru fiecare button
    public boolean viewAdminTable;
    @FXML protected Button btnIntPrez;
    @FXML protected Button btnVizPrez;
    @FXML protected Button btnVizAdminPrez;

    @FXML protected List<RadioButton> buttons = new ArrayList<>();
    @FXML protected List<String> labels = List.of("Curs", "Laborator");
    @FXML protected ComboBox<String> cmb = new ComboBox<String>();
    @FXML protected ToggleGroup group = new ToggleGroup();
    @FXML protected TextField numeField = new TextField();

    // Pop-over pentru introducere prezente
    @FXML protected void onHelloButtonClick() {
        // creeam un layout vertical pentru a ordona frumos elementele
        VBox box = new VBox(15);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(7.5);

        // creeam combo box-ul pentru materii
        if (cmb.getItems().isEmpty()) {
            cmb.getItems().addAll(
                "Programare orientata obiect"
            );
        }

        // creeam un grup de butoane radio (o singura alegere) pentru materii
        if (group.getToggles().isEmpty()) {
            for (String label : labels) {
                RadioButton rb = new RadioButton(label);
                rb.setToggleGroup(group);
                buttons.add(rb);
            }
        }

        // adaugam elemente ca "noduri"
        if (box.getChildren().isEmpty()) {
            box.getChildren().addAll(
                    new Label("Nume:"),
                    numeField,
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

            // creeam butonul de Submit
            Button btnSubmit = new Button("Prezent!");
            btnSubmit.setDefaultButton(true);
            btnSubmit.setOnAction(_ -> {
                writeElevToFile();
            });

            // adaugam un button in layout-ul orizontal
            btnPrezentBox.getChildren().add(btnSubmit);

            // adaugam HBox-ul nostru in layout-ul principal
            box.getChildren().addAll(
                    new Separator(),
                    btnPrezentBox
            );
        }
        // creeam Pop-overul cu continutul layout-ului "box" si il afisam ca copil/sub-nod al lui "btnIntPrez"
        PopOver popOver = new PopOver(box);
        popOver.setTitle("Introducere prezenta"); //setam un titlu in caz ca popover-ul este detasat (tras cu mouse-ul)
        popOver.show(btnIntPrez);
    }

    @FXML protected void onVizPrezClick() throws IOException {
        viewAdminTable = false;
        PrezentUIApp.startTabel( new Stage() );
    }

    // Pop-over pentru vizualizare prezente (admin)
    @FXML protected void onVizPrezAdminClick() {
        // creeam un layout vertical
        VBox box = new VBox(15);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(7.5);

        // creeam un layout orizontal inauntrul layout-ului nostru pentru a aseza butonul pe partea dreapta a
        // ferestrei si adaugam butonul
        HBox btnPrezentBox = new HBox();
        btnPrezentBox.setAlignment(CENTER_RIGHT);

        if (btnPrezentBox.getChildren().isEmpty()) {
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
        }

        // creeam Pop-overul cu continutul layout-ului "box" si il afisam ca copil/sub-nod al lui "btnIntPrez"
        PopOver popOver = new PopOver(box);
        popOver.setTitle("Autentificare"); //setam un titlu in caz ca popover-ul este detasat (tras cu mouse-ul)
        popOver.show(btnVizAdminPrez);
    }

    @FXML protected void writeElevToFile() {
        Optional<ButtonType> result = Optional.empty();
        Dialog<ButtonType> dialog;
        if (Objects.equals(numeField.getText(), null)) {
            // fara nume, da eroare
            dialog = new Dialog<>();
            dialog.setHeaderText("Eroare!");
            dialog.setContentText("Baga macar un nume ba! Hai ma nu fi lenes!");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            result = dialog.showAndWait();
        } else if (Objects.equals(cmb.getSelectionModel().getSelectedItem(), null)) {
            // fara materie, da eroare
            dialog = new Dialog<>();
            dialog.setHeaderText("Eroare!");
            dialog.setContentText("Cum sa fi prezent la ora daca nu ai nici-o materie??");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            result = dialog.showAndWait();
        }

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // nu facem nimica pentru ca e eroare
        } else {
            IO.println("yay");
            RadioButton __curs = (RadioButton) group.getSelectedToggle();

            String line = String.join(",",
                    Objects.toString(LocalDateTime.now()),
                    numeField.getText(),
                    cmb.getSelectionModel().getSelectedItem(),
                    (Objects.equals(__curs.getText(), labels.get(0)) ? "PREZENT" : " "),
                    (Objects.equals(__curs.getText(), labels.get(1)) ? "PREZENT" : " "));

            IO.println(line);

            try (BufferedWriter writer =
                         Files.newBufferedWriter(Paths.get("data.csv"),
                                 StandardOpenOption.CREATE,
                                 StandardOpenOption.APPEND)) {

                writer.write(line);
                writer.newLine();
                IO.println(writer.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // resetam Text Field-ul la urma pentru a preveni erori
        numeField.setText(null);
    }
}
