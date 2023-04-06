package ru.sartfoms.mostat.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import ru.sartfoms.mostat.entity.ReportType;

public class ExcelTemplateGenerator {
	protected XSSFWorkbook template;
	protected XSSFSheet sheet;
	public final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	protected final static String HEADER_DELIMETER = "::";
	XSSFRow row;
	XSSFRow row2;
	CellStyle headerStyle;

	public ExcelTemplateGenerator(ReportType reportType) {
		template = new XSSFWorkbook();
		sheet = template.createSheet("Отчет №" + reportType.getId());
		row = sheet.createRow(0);
		row2 = sheet.createRow(3);
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
		Map<String, Collection<String>> model = createModel(reportType);
		for (Entry<String, Collection<String>> entry : model.entrySet()) {
			if (entry.getValue() != null) {
				setCellValue(createCellAndFormat(row, cellNumber, headerStyle), entry.getKey());
				oldCellNumber = cellNumber;
				for (Iterator<String> iterator = entry.getValue().iterator(); iterator.hasNext();) {
					String bottom = (String) iterator.next();
					setCellValue(createCellAndFormat(row2, cellNumber, headerStyle), bottom);
					sheet.addMergedRegion(new CellRangeAddress(3, 5, cellNumber, cellNumber));
					if (iterator.hasNext())
						cellNumber++;
				}
				sheet.addMergedRegion(new CellRangeAddress(0, 2, oldCellNumber, cellNumber));
			} else {
				setCellValue(createCellAndFormat(row, cellNumber, headerStyle), entry.getKey());
				sheet.addMergedRegion(new CellRangeAddress(0, 5, cellNumber, cellNumber));
			}
			cellNumber++;
		}
	}

	private Map<String, Collection<String>> createModel(ReportType reportType) {
		Map<String, Collection<String>> model = new LinkedHashMap<>();
		buildModel(reportType.getE(), "", model);
		buildModel(reportType.getF(), reportType.getE(), model);
		buildModel(reportType.getG(), reportType.getF(), model);
		buildModel(reportType.getH(), reportType.getG(), model);
		buildModel(reportType.getI(), reportType.getH(), model);
		buildModel(reportType.getJ(), reportType.getI(), model);
		buildModel(reportType.getK(), reportType.getJ(), model);
		buildModel(reportType.getL(), reportType.getK(), model);
		buildModel(reportType.getM(), reportType.getL(), model);
		buildModel(reportType.getN(), reportType.getM(), model);
		buildModel(reportType.getO(), reportType.getN(), model);
		buildModel(reportType.getP(), reportType.getO(), model);
		buildModel(reportType.getQ(), reportType.getP(), model);
		buildModel(reportType.getR(), reportType.getQ(), model);
		buildModel(reportType.getS(), reportType.getR(), model);
		buildModel(reportType.getT(), reportType.getS(), model);
		buildModel(reportType.getU(), reportType.getT(), model);
		buildModel(reportType.getV(), reportType.getU(), model);
		buildModel(reportType.getW(), reportType.getV(), model);
		buildModel(reportType.getX(), reportType.getW(), model);
		buildModel(reportType.getY(), reportType.getX(), model);
		buildModel(reportType.getZ(), reportType.getY(), model);
		buildModel(reportType.getAa(), reportType.getZ(), model);
		buildModel(reportType.getAb(), reportType.getAa(), model);
		buildModel(reportType.getAc(), reportType.getAb(), model);
		buildModel(reportType.getAd(), reportType.getAc(), model);
		buildModel(reportType.getAe(), reportType.getAd(), model);
		buildModel(reportType.getAf(), reportType.getAe(), model);
		buildModel(reportType.getAg(), reportType.getAf(), model);
		buildModel(reportType.getAh(), reportType.getAg(), model);
		buildModel(reportType.getAi(), reportType.getAh(), model);
		buildModel(reportType.getAj(), reportType.getAi(), model);
		buildModel(reportType.getAk(), reportType.getAj(), model);
		buildModel(reportType.getAl(), reportType.getAk(), model);
		buildModel(reportType.getAm(), reportType.getAl(), model);
		buildModel(reportType.getAn(), reportType.getAm(), model);
		buildModel(reportType.getAo(), reportType.getAn(), model);
		buildModel(reportType.getAp(), reportType.getAo(), model);
		buildModel(reportType.getAq(), reportType.getAp(), model);
		buildModel(reportType.getAr(), reportType.getAq(), model);
		buildModel(reportType.getAs(), reportType.getAr(), model);
		buildModel(reportType.getAt(), reportType.getAs(), model);
		buildModel(reportType.getAu(), reportType.getAt(), model);
		buildModel(reportType.getAv(), reportType.getAu(), model);
		buildModel(reportType.getAw(), reportType.getAv(), model);
		buildModel(reportType.getAx(), reportType.getAw(), model);
		buildModel(reportType.getAy(), reportType.getAx(), model);
		buildModel(reportType.getAz(), reportType.getAy(), model);
		buildModel(reportType.getBa(), reportType.getAz(), model);
		buildModel(reportType.getBb(), reportType.getBa(), model);

		return model;
	}

	private void buildModel(String cellValue, String prevValue, Map<String, Collection<String>> model) {
		if (cellValue.contains(HEADER_DELIMETER)) {
			if (cellValue.split(HEADER_DELIMETER)[0].equals(prevValue.split(HEADER_DELIMETER)[0])) {
				model.get(cellValue.split(HEADER_DELIMETER)[0]).add(cellValue.split(HEADER_DELIMETER)[1]);
			} else {
				Collection<String> block = new ArrayList<>();
				block.add(cellValue.split(HEADER_DELIMETER)[1]);
				model.put(cellValue.split(HEADER_DELIMETER)[0], block);
			}
		} else {
			model.put(cellValue, null);
		}

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
