/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import controller.ServiceProceduresControl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import javafx.collections.ObservableList;
import model.ServiceProcedure;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author manolo
 */
public abstract class GenerateExcel {

    private static String month = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, StartApplication.REGION);
    private static HSSFWorkbook workbook = new HSSFWorkbook();
    private static HSSFSheet sheetClients = workbook.createSheet("Procedimentos");

    public static void createFile(File file, ObservableList<ServiceProcedure> procedures) {
        sheetClients.setDefaultColumnWidth(20);
        sheetClients.setDefaultRowHeight((short) 600);
        int rownum = 2;
        Row rowTittles = sheetClients.createRow(0);
        Cell cellTittles = rowTittles.createCell(0);
        cellTittles.setCellStyle(styleHeaderBottom(14));
        cellTittles.setCellValue("Relatórios de Procedimentos - " + month);
        Row rowSubTittles = sheetClients.createRow(1);
        String[] subTittles = {"Data", "Cliente", "Procedimento", "Valor Total", "Valor Recebido"};
        for (int i = 0; i <= 4; i++) {
            Cell cell = rowSubTittles.createCell(i);
            cell.setCellStyle(styleHeaderBottom(12));
            cell.setCellValue(subTittles[i]);
        }
        for (ServiceProcedure procedure : procedures) {
            int cellnum = 0;
            Row row = sheetClients.createRow(rownum++);
            Cell cellDate = row.createCell(cellnum++);
            cellDate.setCellStyle(styleBody());
            cellDate.setCellValue(procedure.getDateServiceFormated());
            Cell cellClient = row.createCell(cellnum++);
            cellClient.setCellStyle(styleBody());
            cellClient.setCellValue(procedure.getClient());
            Cell cellProcedure = row.createCell(cellnum++);
            cellProcedure.setCellStyle(styleBody());
            cellProcedure.setCellValue(procedure.getProcedure());
            Cell cellPrice = row.createCell(cellnum++);
            cellPrice.setCellStyle(styleBody());
            cellPrice.setCellValue(procedure.getPriceFormated());
            Cell cellReceived = row.createCell(cellnum++);
            cellReceived.setCellStyle(styleBody());
            cellReceived.setCellValue(procedure.getReceivedFormated());
        }
        Row rowTotalReceived = sheetClients.createRow(procedures.size() + 2);
        Cell cellTotal = rowTotalReceived.createCell(3);
        cellTotal.setCellStyle(styleBody());
        cellTotal.setCellValue("Total:");
        cellTotal.setCellStyle(styleHeaderBottom(12));
        Cell cellTotalReceived = rowTotalReceived.createCell(4);
        cellTotalReceived.setCellStyle(styleHeaderBottom(12));
        cellTotalReceived.setCellValue(NumberFormat.getCurrencyInstance()
                .format(ServiceProceduresControl.getTotalReceived()));
        sheetClients.addMergedRegion(new CellRangeAddress(0,0,0,4));
        try {
            FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath() + month + ".xls"));
            workbook.write(out);
            out.close();
            System.out.println("Arquivo Excel criado com sucesso!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Arquivo não encontrado!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro na edição do arquivo!");
        }

    }

    private static CellStyle styleHeaderBottom(int fontSize) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) fontSize);
        headerStyle.setFont(font);
        return headerStyle;
    }

    private static CellStyle styleBody() {
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        bodyStyle.setBorderBottom(BorderStyle.MEDIUM);
        bodyStyle.setBorderLeft(BorderStyle.MEDIUM);
        bodyStyle.setBorderRight(BorderStyle.MEDIUM);
        bodyStyle.setBorderTop(BorderStyle.MEDIUM);
        return bodyStyle;
    }

}