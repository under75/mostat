package ru.sartfoms.mostat.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.util.CellRangeAddress;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportType;

public class ExcelTemplateGenerator extends ExcelGenerator {

	public ExcelTemplateGenerator(ReportType reportType, Lpu lpu) {
		super(reportType, lpu);
		createHeader(reportType);
	}

	public ByteArrayInputStream toExcel() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		template.write(out);

		return new ByteArrayInputStream(out.toByteArray());
	}

	@Override
	public ExcelGenerator createHeader(ReportType reportType) {
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

		return this;
	}
}
