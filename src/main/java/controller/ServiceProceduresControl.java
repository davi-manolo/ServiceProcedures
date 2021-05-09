/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ServiceProcedure;
import service.GenerateExcel;
import service.LoadProceduresProperty;

/**
 *
 * @author manolo
 */
public class ServiceProceduresControl implements Initializable {

    @FXML
    private Label money;

    @FXML
    private Text message;

    @FXML
    private DatePicker dateProcess;

    @FXML
    private ComboBox<String> procedures;

    @FXML
    private TextField clientName, priceProcess;

    @FXML
    private Button addProcess, removeProcess, export;

    @FXML
    private TableView<ServiceProcedure> tableServiceProcedures;

    @FXML
    private TableColumn<ServiceProcedure, String> dateColumn, clientColumn,
            procedureColumn, totalPriceColumn, receivedPriceColumn;

    private LoadProceduresProperty loadProcedure = LoadProceduresProperty.getInstance();

    private static double totalReceived;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        message.setText(null);
        addListener(priceProcess);
        loadNodes();
        defineTable();
        addProcess.disableProperty().bind(dateProcess.valueProperty().isNull()
                .or(clientName.textProperty().isEmpty())
                .or(priceProcess.textProperty().isEmpty())
                .or(procedures.getSelectionModel().selectedItemProperty().isNull())
        );
        addProcess.setOnAction(action -> {
            ServiceProcedure process = new ServiceProcedure(
                    dateProcess.getValue(),
                    Double.valueOf(priceProcess.getText()),
                    clientName.getText(),
                    procedures.getSelectionModel().getSelectedItem());
            tableServiceProcedures.getItems().add(process);
            money.setText(totalReceived());
            cleanAll();
        });
        removeProcess.disableProperty().bind(tableServiceProcedures.getSelectionModel()
                .selectedItemProperty().isNull());
        removeProcess.setOnAction(action -> {
            ObservableList<ServiceProcedure> selectedItems
                    = tableServiceProcedures.getSelectionModel().getSelectedItems();
            if (tableServiceProcedures.getItems().containsAll(selectedItems)) {
                tableServiceProcedures.getItems().removeAll(selectedItems);
                money.setText(totalReceived());
            }
        });
        export.disableProperty().bind(Bindings.isEmpty(tableServiceProcedures.getItems()));
        export.setOnAction(action -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter
                    = new FileChooser.ExtensionFilter("Microsoft Excel", "*.xls");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(new Stage());
            GenerateExcel.createFile(file, tableServiceProcedures.getItems());
            message.setText("Arquivo gerado com sucesso!");
        });
    }

    private void loadNodes() {
        try {

            loadProcedure.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadProcedure.getKeys().forEach(procedure -> procedures.getItems().add(procedure));
    }

    private void defineTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateServiceFormated"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        procedureColumn.setCellValueFactory(new PropertyValueFactory<>("procedure"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceFormated"));
        receivedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("receivedFormated"));
        tableServiceProcedures.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void cleanAll() {
        dateProcess.setValue(null);
        clientName.clear();
        priceProcess.clear();
        procedures.setValue(null);
    }

    private String totalReceived() {
        double total = 0;
        for (ServiceProcedure service : tableServiceProcedures.getItems()) {
            total += service.getReceived();
        }
        totalReceived = total;
        return NumberFormat.getCurrencyInstance().format(total);
    }

    public static double getTotalReceived() {
        return totalReceived;
    }

    private void addListener(TextField textfield) {
        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textfield.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

}
