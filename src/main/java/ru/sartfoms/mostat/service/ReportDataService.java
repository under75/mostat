package ru.sartfoms.mostat.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.sartfoms.mostat.entity.ReportData;
import ru.sartfoms.mostat.entity.ReportStatus;
import ru.sartfoms.mostat.entity.ReportStatusId;
import ru.sartfoms.mostat.entity.User;
import ru.sartfoms.mostat.repository.ReportDataRepository;
import ru.sartfoms.mostat.repository.ReportStatusRepository;
import ru.sartfoms.mostat.repository.VReportDataRepository;
import ru.sartfoms.mostat.service.util.Status;

@Service
public class ReportDataService {
	private final ReportDataRepository reportDataRepository;
	private final ReportStatusRepository reportStatusRepository;

	public ReportDataService(ReportDataRepository reportDataRepository, ReportStatusRepository reportStatusRepository,
			VReportDataRepository vReportDataRepository) {
		this.reportDataRepository = reportDataRepository;
		this.reportStatusRepository = reportStatusRepository;
	}

	public Boolean parseAndSave(MultipartFile[] files, User user, String dtRepStr) {
		Boolean hasError = false;
		try {
			LocalDate dtRep = LocalDate.parse(dtRepStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					InputStream inputStream = new ByteArrayInputStream(file.getBytes());
					ExcelParser parser = new ExcelParser(inputStream);
					Iterator<Row> iterator = parser.getRowIterator();
					while (iterator.hasNext()) {
						Row row = iterator.next();
						if (row.getRowNum() >= ExcelParser.START_ROW_NUM) {
							ReportData reportData = new ReportData();
							reportData.setUserName(user.getName());
							reportData.setLpuId(user.getLpuId());
							reportData.setDtRep(dtRep);
							reportData.setDtIns(LocalDateTime.now());
							reportData.setFile(file.getBytes());
							if (parser.write(reportData, row))
								reportDataRepository.save(reportData);
						}
					}
					ReportStatusId reportStatusId = new ReportStatusId(parser.getRepTypeId(), user.getLpuId(), dtRep);
					ReportStatus reportStatus = reportStatusRepository.getReferenceById(reportStatusId);
					reportStatus.setStatus(Status.DONE);
					reportStatusRepository.save(reportStatus);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			hasError = true;
		}

		return hasError;
	}

	public InputStream getReport(Long typeId, LocalDate dateTime, Integer lpuId) {
		ReportData report = reportDataRepository.findByTypeIdAndLpuIdAndDtRep(typeId, lpuId, dateTime).stream()
				.findAny().get();
		byte[] reportFile = null;
		if (report != null) {
			reportFile = report.getFile();
		} else {
			throw new NullPointerException();
		}

		return new ByteArrayInputStream(reportFile);
	}

	public void delete(Long typeId, String dateTimeStr) {
		ReportData reportData = reportDataRepository.findByTypeIdAndDtRep(typeId, LocalDate.parse(dateTimeStr));

		reportDataRepository.delete(reportData);

	}

}
