package ru.sartfoms.mostat.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ru.sartfoms.mostat.entity.ReportData;

public class ExcelParser {
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	public static final int START_ROW_NUM = 8;

	public ExcelParser(InputStream inputStream) throws IOException {
		workbook = new XSSFWorkbook(inputStream);
		sheet = workbook.getSheetAt(0);
	}

	public Iterator<Row> getRowIterator() {
		return sheet.iterator();
	}

	public Boolean write(ReportData entity, Row row) throws IOException {
		String sheetName = sheet.getSheetName();
		entity.setTypeId(Long.valueOf(sheetName.substring(sheetName.indexOf("№") + 1)));

		if (row.getRowNum() < START_ROW_NUM)
			return false;

		entity.setRowNum(row.getRowNum() - START_ROW_NUM + 1);
		int colNum = 0;
		entity.setE(getValue(row.getCell(colNum)));
		entity.setF(getValue(row.getCell(++colNum)));
		entity.setG(getValue(row.getCell(++colNum)));
		entity.setH(getValue(row.getCell(++colNum)));
		entity.setI(getValue(row.getCell(++colNum)));
		entity.setJ(getValue(row.getCell(++colNum)));
		entity.setK(getValue(row.getCell(++colNum)));
		entity.setL(getValue(row.getCell(++colNum)));
		entity.setM(getValue(row.getCell(++colNum)));
		entity.setN(getValue(row.getCell(++colNum)));
		entity.setO(getValue(row.getCell(++colNum)));
		entity.setP(getValue(row.getCell(++colNum)));
		entity.setQ(getValue(row.getCell(++colNum)));
		entity.setR(getValue(row.getCell(++colNum)));
		entity.setS(getValue(row.getCell(++colNum)));
		entity.setT(getValue(row.getCell(++colNum)));
		entity.setU(getValue(row.getCell(++colNum)));
		entity.setV(getValue(row.getCell(++colNum)));
		entity.setW(getValue(row.getCell(++colNum)));
		entity.setX(getValue(row.getCell(++colNum)));
		entity.setY(getValue(row.getCell(++colNum)));
		entity.setZ(getValue(row.getCell(++colNum)));
		entity.setAa(getValue(row.getCell(++colNum)));
		entity.setAb(getValue(row.getCell(++colNum)));
		entity.setAc(getValue(row.getCell(++colNum)));
		entity.setAd(getValue(row.getCell(++colNum)));
		entity.setAe(getValue(row.getCell(++colNum)));
		entity.setAf(getValue(row.getCell(++colNum)));
		entity.setAg(getValue(row.getCell(++colNum)));
		entity.setAh(getValue(row.getCell(++colNum)));
		entity.setAi(getValue(row.getCell(++colNum)));
		entity.setAj(getValue(row.getCell(++colNum)));
		entity.setAk(getValue(row.getCell(++colNum)));
		entity.setAl(getValue(row.getCell(++colNum)));
		entity.setAm(getValue(row.getCell(++colNum)));
		entity.setAn(getValue(row.getCell(++colNum)));
		entity.setAo(getValue(row.getCell(++colNum)));
		entity.setAp(getValue(row.getCell(++colNum)));
		entity.setAq(getValue(row.getCell(++colNum)));
		entity.setAr(getValue(row.getCell(++colNum)));
		entity.setAs(getValue(row.getCell(++colNum)));
		entity.setAt(getValue(row.getCell(++colNum)));
		entity.setAu(getValue(row.getCell(++colNum)));
		entity.setAv(getValue(row.getCell(++colNum)));
		entity.setAw(getValue(row.getCell(++colNum)));
		entity.setAx(getValue(row.getCell(++colNum)));
		entity.setAy(getValue(row.getCell(++colNum)));
		entity.setAz(getValue(row.getCell(++colNum)));
		entity.setBa(getValue(row.getCell(++colNum)));
		entity.setBb(getValue(row.getCell(++colNum)));
		
		return true;
	}

	public String getValue(Cell cell) {
		String cellValue = null;
		try {
			switch (cell.getCellType()) {
			case NUMERIC:
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			case STRING:
				cellValue = cell.getStringCellValue();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cellValue;
	}
}
