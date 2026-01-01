package org.uav.prezentui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static javafx.geometry.Pos.CENTER_RIGHT;

public class PrezenteTabelController {
    @FXML protected TitledPane titledpane;
    @FXML protected FlowPane hbox;
    @FXML protected TableView<Person> tblView;
    @FXML protected TextField txtFieldLabel;
    @FXML protected TableColumn<Person, LocalDateTime> dateColumn;
    @FXML protected TableColumn<Person, String> nameColumn, cursColumn, labColumn, tblMaterie;
    @FXML protected FlowPane statusBar;
    @FXML protected VBox parentVbox;

    // daca suntem admin, adaugam/modificam chestii inainte de a fi afisata fereastra
    private TextField numeField = new TextField();
    private ComboBox<String> cmb = new ComboBox<String>();
    private ToggleGroup group = new ToggleGroup();
    private List<CheckBox> tipPrez = List.of(
            new CheckBox("Curs"),
            new CheckBox("Laborator")
    );
    private DatePicker date = new DatePicker();
    private Button prezentBtn = new Button("Pune prezent");

    @FXML private void initialize() {
        cmb.getItems().addAll(
            "Programare orientata obiect"
        );
        prezentBtn.setDefaultButton(true);
        prezentBtn.setOnAction(_ -> writeElevToFile());
        if (Launcher.viewAdminTable) {
            VBox hboxcontent = new VBox();
            hboxcontent.setSpacing(10);
            titledpane.setText("Vizualizare prezente (Admin.)");
            hboxcontent.getChildren().addAll(
                new Label("Nume:"),
                numeField,
                new Separator(),
                new Label("Materie: "),
                cmb,
                new Separator(),
                new Label("Data:"),
                date,
                new Separator()
            );
            hboxcontent.getChildren().addAll(tipPrez);

            HBox btnPrezentBox = new HBox();
            btnPrezentBox.setAlignment(CENTER_RIGHT);
            btnPrezentBox.getChildren().add(prezentBtn);

            hboxcontent.getChildren().add(btnPrezentBox);

            hbox.getChildren().add( hboxcontent );
        }
        initTabel();
    }

    @FXML public void initTabel() {
        // creeam coloanele in tabel
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cursColumn.setCellValueFactory(new PropertyValueFactory<>("curs"));
        labColumn.setCellValueFactory(new PropertyValueFactory<>("lab"));
        tblMaterie.setCellValueFactory(new PropertyValueFactory<>("materie"));

        // adaugam coloanele in tabel pt a adauga date
        ObservableList<Person> data = FXCollections.observableArrayList();
        tblView.setItems(data);

        // TODO: adaugam date in tabel, filtrate pe baza numelui introdus in txtFieldLabel
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if ((Objects.equals(values[1], txtFieldLabel.getText()) && (!Launcher.viewAdminTable || (Launcher.viewAdminTable && !txtFieldLabel.getText().isBlank()))))
                    data.add(new Person(values[0], values[1], values[2], values[3], values[4]));
                else if (Launcher.viewAdminTable && txtFieldLabel.getText().isBlank())
                    data.add(new Person(values[0], values[1], values[2], values[3], values[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: status bar pentru nr de prezente per materie + total

        ArrayList<Label> prez = new ArrayList<>();

        statusBar.setHgap(20); statusBar.setVgap(10);
        statusBar.getChildren().clear();
        int rowCount = tblView.getItems().size();

        /*
        for (int i = 0; i < rowCount; i++) {
            IO.println( tblView.getItems().get(i).getMaterie() );
        }
        */

        prez.add( new Label("Nr. prezente totale: " + rowCount) );

        statusBar.getChildren().addAll( prez );

        IO.println(rowCount);
    }

    // functia de scriere in CSV-ul prezentelor, modificata
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
                    Objects.toString(date.getValue()),
                    numeField.getText(),
                    cmb.getSelectionModel().getSelectedItem(),
                    (Objects.equals(tipPrez.get(0).isSelected(), true) ? "PREZENT" : " "),
                    (Objects.equals(tipPrez.get(1).isSelected(), true) ? "PREZENT" : " "));

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
        initTabel();
    }
}
