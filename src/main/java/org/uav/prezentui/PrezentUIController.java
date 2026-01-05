package org.uav.prezentui;

import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.synedra.validatorfx.Check;
import org.controlsfx.control.PopOver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static javafx.geometry.Pos.*;

// Clasa care controleaza elementele (butoane, meniuri etc) aplicatiei
public class PrezentUIController {
    public String password = "UAV2025";

    // ID-uri pentru fiecare button
    @FXML protected Button btnIntPrez;
    @FXML protected Button btnVizPrez;
    @FXML protected Button btnVizAdminPrez;

    @FXML protected List<RadioButton> buttons = new ArrayList<>();
    @FXML protected List<String> labels = List.of("Curs", "Laborator");
    @FXML protected ComboBox<String> cmb = new ComboBox<String>();
    @FXML protected ToggleGroup group = new ToggleGroup();
    @FXML protected TextField numeField = new TextField();
    @FXML protected PasswordField __psfl = new PasswordField();
    @FXML protected List<CheckBox> tipPrez = List.of(
      new CheckBox("Curs"),
      new CheckBox("Laborator")
    );

    // Pop-over pentru introducere prezente
    @FXML public void onHelloButtonClick() {
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
            box.getChildren().addAll(tipPrez);

            // creeam un layout orizontal inauntrul layout-ului nostru pentru a aseza butonul pe partea dreapta a
            // ferestrei si adaugam butonul
            HBox btnPrezentBox = new HBox();
            btnPrezentBox.setAlignment(CENTER_RIGHT);

            // creeam butonul de Submit
            Button btnSubmit = new Button("Prezent!");
            btnSubmit.setDefaultButton(true);
            btnSubmit.setOnAction(_ -> writeElevToFile());

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

    @FXML public void onVizPrezClick() throws IOException {
        org.uav.prezentui.Launcher.viewAdminTable = false;
        PrezentUIApp.startTabel( new Stage() );
    }

    // Pop-over pentru vizualizare prezente (admin)
    @FXML public void onVizPrezAdminClick() {
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
            Button __btn = new Button("Submite si intra");
            btnPrezentBox.getChildren().add(__btn);
            __btn.setDefaultButton(true);
            __btn.setOnAction(_ -> {
                try {
                    viewAdminTableStage(__psfl.getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // adaugam elemente in layout
            box.getChildren().addAll(
                    new Label("ATENTIE! Aceaste portiune este menita doar pentru profesori si administratori!"),
                    new Separator(),
                    new Label("Parola:"),
                    __psfl,
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
            
            String name = numeField.getText();
            String materie = cmb.getSelectionModel().getSelectedItem();
            boolean curs = Objects.equals(tipPrez.get(0).isSelected(), true);
            boolean laborator = Objects.equals(tipPrez.get(1).isSelected(), true);

            // Deschide quiz, prezenta se trece doar daca se raspunde corect la o intrebare
            QuizController quizController = new QuizController();
            quizController.showQuiz(() -> {
            	
            	String line = String.join(",",
                        Objects.toString(LocalDate.now()),
                        name,
                        materie,
                        (curs ? "PREZENT" : " "),
                        (laborator ? "PREZENT" : " "));

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
            });
        }
        // resetam Text Field-ul la urma pentru a preveni erori
        numeField.setText(null);
        cmb.getSelectionModel().clearSelection();
        tipPrez.get(0).setSelected(false);
        tipPrez.get(1).setSelected(false);
    }

    @FXML public void viewAdminTableStage(String pass) throws IOException {
        if (Objects.equals(pass, password)) {
            org.uav.prezentui.Launcher.viewAdminTable = true;
            PrezentUIApp.startTabel( new Stage() );
        } else {
            Dialog<ButtonType> dialog;
            if (pass.isBlank()) {
                // fara parola, da eroare
                dialog = new Dialog<>();
                dialog.setHeaderText("Eroare!");
                dialog.setContentText("Nici-o parola inserata.");
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.showAndWait();
            } else {
                // parola gresita, da eroare
                dialog = new Dialog<>();
                dialog.setHeaderText("Eroare!");
                dialog.setContentText("Parola este incorecta.");
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.showAndWait();
            }
        }
    }
}
