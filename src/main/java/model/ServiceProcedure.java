/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import service.LoadProceduresProperty;

/**
 *
 * @author manolo
 */
public class ServiceProcedure {

    private LocalDate dateService;
    private Double price, received;
    private String priceFormated, receivedFormated, dateServiceFormated;
    private String client, procedure;
    private LoadProceduresProperty loadProcedure = LoadProceduresProperty.getInstance();

    public ServiceProcedure(LocalDate dateService, Double price, String client, String procedure) {
        this.dateService = dateService;
        this.price = price;
        this.received = (price / 100) * loadProcedure.getValue(procedure);
        this.client = client;
        this.procedure = procedure;
        this.receivedFormated = NumberFormat.getCurrencyInstance().format(received);
        this.dateServiceFormated = dateService.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public void setDateService(LocalDate dateService) {
        this.dateService = dateService;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getDateServiceFormated() {
        return dateServiceFormated;
    }

    public LocalDate getDateService() {
        return dateService;
    }

    public Double getPrice() {
        return price;
    }

    public String getPriceFormated() {
        return priceFormated;
    }

    public Double getReceived() {
        return received;
    }

    public String getReceivedFormated() {
        return receivedFormated;
    }

    public String getClient() {
        return client;
    }

    public String getProcedure() {
        return procedure;
    }

}
