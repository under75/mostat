package ru.sartfoms.mostat.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportType;
import ru.sartfoms.mostat.service.util.DateService;

public class ExcelReportGenerator extends ExcelGenerator {
	private int rowNum = 8;// first data row

	public ExcelReportGenerator(ReportType reportType, Lpu lpu) {
		super(reportType, lpu);
		createHeader(reportType);
	}

	public ExcelReportGenerator createBody(
			Map<Integer, Map<LocalDateTime, Map<Integer, Collection<String>>>> dataModel) {
		XSSFRow dataRow = sheet.createRow(rowNum);
		for (Entry<Integer, Map<LocalDateTime, Map<Integer, Collection<String>>>> lpuEntry : dataModel.entrySet()) {
			int cellNumber = 0;
			setCellValue(createCellAndFormat(dataRow, cellNumber, headerStyle), String.valueOf(lpuEntry.getKey()));
			cellNumber++;
			for (Entry<LocalDateTime, Map<Integer, Collection<String>>> dateEntry : lpuEntry.getValue().entrySet()) {
				setCellValue(createCellAndFormat(dataRow, cellNumber, headerStyle),
						dateEntry.getKey().format(DateService.DATE_TIME_FORMATTER));
				cellNumber++;
				for (Entry<Integer, Collection<String>> rowEntry : dateEntry.getValue().entrySet()) {
					for (String value : rowEntry.getValue()) {
						setCellValue(createCellAndFormat(dataRow, cellNumber, bodyStyle), value);
						cellNumber++;
					}
					cellNumber = 2;
					rowNum++;
					dataRow = sheet.createRow(rowNum);
				}
			}
		}

		return this;
	}

	@Override
	public ExcelGenerator createHeader(ReportType reportType) {
		int cellNumber = 0;
		int oldCellNumber;
		setCellValue(createCellAndFormat(row0, cellNumber, titleStyle), lpu.getName());
		setCellValue(createCellAndFormat(row1, cellNumber, titleStyle),
				"Отчет №" + reportType.getId() + " " + reportType.getName());
		setCellValue(createCellAndFormat(row2, cellNumber, headerStyle), "Код МО");
		sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum() + 5, cellNumber, cellNumber));
		cellNumber++;
		setCellValue(createCellAndFormat(row2, cellNumber, headerStyle), "Дата отчета");
		sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum() + 5, cellNumber, cellNumber));
		cellNumber++;

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

		for (int i = 0; i < 52; i++) {
			sheet.autoSizeColumn(i);
		}

		return this;
	}

	public ExcelGenerator createSumRow(Collection<Double> sumRowData) {
		XSSFRow sumRow = sheet.createRow(rowNum);
		sumRow.setRowStyle(sumRowStyle);
		int cellNumber = 0;
		setCellValue(createCellAndFormat(sumRow, cellNumber, sumRowStyle), "Сумма:");
		sheet.addMergedRegion(new CellRangeAddress(sumRow.getRowNum(), sumRow.getRowNum(), cellNumber, cellNumber + 1));
		cellNumber++;
		cellNumber++;
		for (Double value : sumRowData) {
			setCellValue(createCellAndFormat(sumRow, cellNumber, sumRowStyle),
					value != null ? String.valueOf(value) : "");
			cellNumber++;
		}

		return this;
	}
}