package ru.sartfoms.mostat.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
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

	public enum UPLOAD_STATUS {
		INVALID, DONE
	}

	@Value("${criptopro.dir}")
	private String criptoproDir;

	public ReportDataService(ReportDataRepository reportDataRepository, ReportStatusRepository reportStatusRepository,
			VReportDataRepository vReportDataRepository) {
		this.reportDataRepository = reportDataRepository;
		this.reportStatusRepository = reportStatusRepository;
	}

	public UPLOAD_STATUS parseAndSave(MultipartFile[] reps, MultipartFile[] signs, User user, String dtRepStr) {
		UPLOAD_STATUS status = null;
		if (!saveAndValidate(reps, signs, user)) {
			return UPLOAD_STATUS.INVALID;
		}
		try {
			LocalDate dtRep = LocalDate.parse(dtRepStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			File file = new File(criptoproDir + "report" + user.getLpuId());
			File signature = new File(criptoproDir + "signature" + user.getLpuId());
			InputStream inputStream = new FileInputStream(file);
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
					reportData.setFile(FileUtils.readFileToByteArray(file));
					reportData.setSignature(FileUtils.readFileToByteArray(signature));
					if (parser.write(reportData, row))
						reportDataRepository.save(reportData);
				}
			}
			ReportStatusId reportStatusId = new ReportStatusId(parser.getRepTypeId(), user.getLpuId(), dtRep);
			ReportStatus reportStatus = reportStatusRepository.getReferenceById(reportStatusId);
			reportStatus.setStatus(Status.DONE);
			reportStatusRepository.save(reportStatus);
			status = UPLOAD_STATUS.DONE;
		} catch (Exception e) {
			e.printStackTrace();
			status = UPLOAD_STATUS.INVALID;
		}

		return status;
	}

	private boolean saveAndValidate(MultipartFile[] reps, MultipartFile[] signs, User user) {
		boolean error = false;
		boolean repSaved = false;
		boolean signSaved = false;
		String report = "report" + user.getLpuId();
		String signature = "signature" + user.getLpuId();
		for (MultipartFile file : reps) {
			if (!file.isEmpty()) {
				String filePath = criptoproDir + report;
				File dest = new File(filePath);
				try {
					file.transferTo(dest);
					repSaved = true;
					break;
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
				}
			}
		}
		for (MultipartFile file : signs) {
			if (!file.isEmpty()) {
				String filePath = criptoproDir + signature;
				File dest = new File(filePath);
				try {
					file.transferTo(dest);
					signSaved = true;
					break;
				} catch (Exception e) {
					e.printStackTrace();
					error = true;
				}
			}
		}
		if (!error && repSaved && signSaved) {
			Process proc;
			try {
				proc = Runtime.getRuntime().exec(criptoproDir
						+ "cryptcp.x64.exe -verify -uTrustedPeople -detached -nochain " + report + " " + signature,
						null, new File(criptoproDir));
				InputStreamReader isr = new InputStreamReader(proc.getInputStream(), Charset.forName("CP866"));
				BufferedReader br = new BufferedReader(isr);
				if (br.lines().filter(t -> t.contains("ErrorCode: 0x00000000")).count() == 0) {
					error = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				error = true;
			}
		} else {
			error = true;
		}

		return !error;
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
