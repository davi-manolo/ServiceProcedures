/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author manolo
 */
public class StartApplication extends Application {

    public static final Locale REGION = new Locale("pt", "BR");

    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/ServiceProcedureUI.fxml"));
            Scene scene = new Scene(root, 1020, 590);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
            primaryStage.setTitle("Procedimentos");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(evt -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Deseja sair?");
                alert.setHeaderText("Deseja realmente sair?");
                alert.showAndWait().filter(r -> r != ButtonType.OK).ifPresent(r -> evt.consume());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(REGION);
        launch(args);
    }

}
