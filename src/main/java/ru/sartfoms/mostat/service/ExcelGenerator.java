package ru.sartfoms.mostat.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportType;

public abstract class ExcelGenerator {
	protected XSSFWorkbook template;
	protected XSSFSheet sheet;
	protected XSSFRow row0;
	protected XSSFRow row1;
	protected XSSFRow row2;
	protected XSSFRow row5;
	protected CellStyle titleStyle;
	protected CellStyle headerStyle;
	protected CellStyle bodyStyle;
	protected CellStyle sumRowStyle;
	protected Lpu lpu;

	public ExcelGenerator(ReportType reportType, Lpu lpu) {
		this.lpu = lpu;
		template = new XSSFWorkbook();
		sheet = template.createSheet("Отчет №" + reportType.getId());
		row0 = sheet.createRow(0);
		row1 = sheet.createRow(1);
		row2 = sheet.createRow(2);
		row5 = sheet.createRow(5);
		createStyle();
	}

	public abstract ExcelGenerator createHeader(ReportType reportType);

	public ByteArrayInputStream toExcel() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		template.write(out);

		return new ByteArrayInputStream(out.toByteArray());
	}

	protected XSSFCell createCellAndFormat(XSSFRow row, Integer index, CellStyle style) {
		XSSFCell cell = row.createCell(index);
		cell.setCellStyle(style);

		return cell;
	}

	protected void setCellValue(XSSFCell cell, Object value) {
		if (value != null) {
			cell.setCellValue((String) value);
		} else {
			cell.setCellValue("");
		}
	}

	private void createStyle() {
		titleStyle = template.createCellStyle();
		Font titleFont = template.createFont();
		titleFont.setFontName("Calibri");
		titleFont.setItalic(true);
		titleStyle.setFont(titleFont);

		headerStyle = template.createCellStyle();
		Font headerFont = template.createFont();
		headerFont.setBold(false);
		headerFont.setFontName("Calibri");
		headerFont.setFontHeightInPoints((short) 10);
		headerStyle.setFont(headerFont);
		headerStyle.setWrapText(true);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		bodyStyle = template.createCellStyle();
		bodyStyle.setFont(headerFont);
		bodyStyle.setWrapText(true);
		bodyStyle.setAlignment(HorizontalAlignment.RIGHT);
		
		
		sumRowStyle = template.createCellStyle();
		Font boldFont = template.createFont();
		boldFont.setBold(true);
		boldFont.setFontName("Calibri");
		boldFont.setFontHeightInPoints((short) 10);
		sumRowStyle.setFont(boldFont);
		sumRowStyle.setAlignment(HorizontalAlignment.RIGHT);
		sumRowStyle.setBorderTop(BorderStyle.DASHED);
	}

}
