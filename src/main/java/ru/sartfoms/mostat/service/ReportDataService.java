package ru.sartfoms.mostat.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.sartfoms.mostat.entity.ReportData;
import ru.sartfoms.mostat.repository.ReportDataRepository;

@Service
public class ReportDataService {
	private final ReportDataRepository reportDataRepository;

	public ReportDataService(ReportDataRepository reportDataRepository) {
		this.reportDataRepository = reportDataRepository;
	}

	public Boolean parseAndSave(MultipartFile[] files) {
		Boolean hasError = false;
		try {
			for (MultipartFile file : files) {
				if (!file.getName().isEmpty()) {
					InputStream inputStream = new ByteArrayInputStream(file.getBytes());
					ExcelParser parser = new ExcelParser(inputStream);
					ReportData reportData = new ReportData();
					parser.fillOut(reportData);

//					reportDataRepository.save(reportData);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			hasError = true;
		}

		return hasError;
	}
}
