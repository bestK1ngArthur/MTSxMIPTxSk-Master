package ru.bestk1ng.java.hw3;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import ru.bestk1ng.java.hw3.db.DBWorker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TableBuilder {
    private final static String OUTPUT_PATH = "./reports/";

    public void buildTable(String title, DBWorker.Report report) {
        try (Workbook book = new HSSFWorkbook()) {
            Sheet sheet = book.createSheet(title);
            Row firstRow = sheet.createRow(0);

            CellStyle style = book.createCellStyle();
            Font font = book.createFont();
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
            style.setLocked(true);

            int j = 0;
            for (String rawCell : report.rowNames) {
                Cell currentCell = firstRow.createCell(j++);
                currentCell.setCellValue(rawCell);
                currentCell.setCellStyle(style);
            }

            int i = 1;
            for (String[] rawRow : report.rows) {
                Row row = sheet.createRow(i++);
                j = 0;
                for (String rawCell : rawRow) {
                    Cell currentCell = row.createCell(j++);
                    currentCell.setCellValue(rawCell);
                }
            }

            for (int x = 0; x < sheet.getRow(0).getPhysicalNumberOfCells(); x++) {
                sheet.autoSizeColumn(x);
            }

            String filePath = OUTPUT_PATH + title + ".xlsx";
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            file.createNewFile();
            book.write(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
