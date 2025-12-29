package org.uav.prezentui;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static org.uav.prezentui.Launcher.viewAdminTable;

public class PrezentUIApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PrezentUIApp.class.getResource("main-UI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 240);
        stage.setTitle("Universitatea de Stiinte Exacte \"Aurel Vlaicu\" Arad - PrezentUI");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void startTabel(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PrezentUIApp.class.getResource("prezente-tabel.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), viewAdminTable ? 925 : 720, 540);
        stage.setTitle("Universitatea de Stiinte Exacte \"Aurel Vlaicu\" Arad - Vizualizare prezente");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
