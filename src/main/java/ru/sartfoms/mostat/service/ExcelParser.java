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
	private static final int FIRST_ROW = 8;

	public ExcelParser(InputStream inputStream) throws IOException {
		workbook = new XSSFWorkbook(inputStream);
		sheet = workbook.getSheetAt(0);
	}

	public void write(ReportData entity) throws IOException {
		String sheetName = sheet.getSheetName();
		entity.setTypeId(Long.valueOf(sheetName.substring(sheetName.indexOf("№")+1)));
		
		Iterator<Row> rowIterator = sheet.iterator();
		Row row;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			if (row.getRowNum() < FIRST_ROW)
				continue;
			
			entity.setRowNum(row.getRowNum() - FIRST_ROW + 1);
			Iterator<Cell> cellIterator = row.cellIterator();
			Cell cell;
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setE(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setF(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setG(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setH(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setI(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setJ(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setK(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setL(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setM(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setN(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setO(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setP(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setQ(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setR(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setS(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setT(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setU(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setV(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setW(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setX(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setY(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setZ(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAa(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAb(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAc(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAd(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAe(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAf(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAg(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAh(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAi(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAj(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAk(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAl(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAm(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAn(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAo(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAp(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAq(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAr(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAs(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAt(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAu(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAv(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAw(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAx(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAy(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setAz(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setBa(getValue(cell));
			}
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
				entity.setBb(getValue(cell));
			}
		}
		workbook.close();
	}
	
	public String getValue(Cell cell) {
		String cellValue = null;
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
		
		return cellValue;
	}
}
