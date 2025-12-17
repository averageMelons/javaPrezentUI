package org.uav.prezentui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.*;
import javafx.scene.layout.FlowPane;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class PrezenteTabelController {
    @FXML protected TableView<Person> tblView;
    @FXML protected TextField txtFieldLabel;
    @FXML protected TableColumn<Person, LocalDateTime> dateColumn;
    @FXML protected TableColumn<Person, String> nameColumn, cursColumn, labColumn, tblMaterie;
    @FXML protected FlowPane statusBar;

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
                if (Objects.equals(values[1], txtFieldLabel.getText())) data.add(new Person(values[0], values[1], values[2], values[3], values[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: status bar pentru nr de prezente per materie + total

        ArrayList<Label> prez = new ArrayList<>();

        statusBar.setHgap(20); statusBar.setVgap(10);
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
}
