package ru.sartfoms.mostat.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportType;

public class ExcelTemplateGenerator {
	protected XSSFWorkbook template;
	protected XSSFSheet sheet;
	public final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	XSSFRow row0;
	XSSFRow row1;
	XSSFRow row2;
	XSSFRow row5;
	CellStyle titleStyle;
	CellStyle headerStyle;
	Lpu lpu;

	public ExcelTemplateGenerator(ReportType reportType, Lpu lpu) {
		this.lpu = lpu;
		template = new XSSFWorkbook();
		sheet = template.createSheet("Отчет №" + reportType.getId());
		row0 = sheet.createRow(0);
		row1 = sheet.createRow(1);
		row2 = sheet.createRow(2);
		row5 = sheet.createRow(5);
		createStyle();
		createHeader(reportType);
	}

	public ByteArrayInputStream toExcel() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		template.write(out);

		return new ByteArrayInputStream(out.toByteArray());
	}

	protected void createHeader(ReportType reportType) {
		int cellNumber = 0;
		int oldCellNumber;
		setCellValue(createCellAndFormat(row0, cellNumber, titleStyle), lpu.getName());
		setCellValue(createCellAndFormat(row1, cellNumber, titleStyle),
				"Отчет №" + reportType.getId() + " " + reportType.getName());
		Map<String, Collection<String>> model = ReportTypeService.createHeaderModel(reportType);
		for (Entry<String, Collection<String>> entry : model.entrySet()) {
			if (entry.getValue() != null) {
				setCellValue(createCellAndFormat(row2, cellNumber, headerStyle), entry.getKey());
				oldCellNumber = cellNumber;
				for (Iterator<String> iterator = entry.getValue().iterator(); iterator.hasNext();) {
					String bottom = (String) iterator.next();
					setCellValue(createCellAndFormat(row5, cellNumber, headerStyle), bottom);
					sheet.addMergedRegion(
							new CellRangeAddress(row5.getRowNum(), row5.getRowNum() + 2, cellNumber, cellNumber));
					if (iterator.hasNext())
						cellNumber++;
				}
				sheet.addMergedRegion(
						new CellRangeAddress(row2.getRowNum(), row2.getRowNum() + 2, oldCellNumber, cellNumber));
			} else {
				setCellValue(createCellAndFormat(row2, cellNumber, headerStyle), entry.getKey());
				sheet.addMergedRegion(
						new CellRangeAddress(row2.getRowNum(), row2.getRowNum() + 5, cellNumber, cellNumber));
			}
			cellNumber++;
		}
		sheet.addMergedRegion(
				new CellRangeAddress(row0.getRowNum(), row0.getRowNum(), row0.getFirstCellNum(), cellNumber - 1));
		sheet.addMergedRegion(
				new CellRangeAddress(row1.getRowNum(), row1.getRowNum(), row1.getFirstCellNum(), cellNumber - 1));
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
		// titleFont.setFontHeightInPoints((short) 10);
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
	}

}
