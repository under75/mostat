package ru.sartfoms.mostat.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportData;
import ru.sartfoms.mostat.entity.ReportType;
import ru.sartfoms.mostat.entity.User;
import ru.sartfoms.mostat.model.FormParameters;
import ru.sartfoms.mostat.model.MOReport;
import ru.sartfoms.mostat.repository.ReportDataRepository;

@Service
public class ReportDataService {
	private static final Integer PAGE_SIZE = 10;
	private final ReportDataRepository reportDataRepository;

	public ReportDataService(ReportDataRepository reportDataRepository) {
		this.reportDataRepository = reportDataRepository;
	}

	public Boolean parseAndSave(MultipartFile[] files, User user) {
		Boolean hasError = false;
		try {
			for (MultipartFile file : files) {
				if (!file.getName().isEmpty()) {

					ReportData reportData = new ReportData();
					reportData.setUserName(user.getName());
					reportData.setLpuId(user.getLpuId());
					reportData.setDateTime(LocalDateTime.now());
					reportData.setFile(file.getBytes());

					InputStream inputStream = new ByteArrayInputStream(file.getBytes());
					ExcelParser parser = new ExcelParser(inputStream);
					parser.write(reportData);//to do

					reportDataRepository.save(reportData);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			hasError = true;
		}

		return hasError;
	}

	public Collection<ReportData> findByTypeId(Long reportTypeId) {
		return reportDataRepository.findByTypeId(reportTypeId);
	}

	public Collection<ReportData> findByTypeIdAndLpuIdAndDateTimeBetween(Long reportTypeId, Integer lpuId,
			String dtFrom, String dtTo) {
		return reportDataRepository.findByTypeIdAndLpuIdAndDateTimeBetweenOrderByDateTime(reportTypeId, lpuId,
				LocalDateTime.parse(dtFrom), LocalDateTime.parse(dtTo));
	}

	public Collection<ReportData> findByTypeIdAndDateTimeBetween(Long reportTypeId, String dtFrom, String dtTo) {
		return reportDataRepository.findByTypeIdAndDateTimeBetweenOrderByDateTime(reportTypeId,
				LocalDateTime.parse(dtFrom), LocalDateTime.parse(dtTo));
	}

	public Collection<ReportData> findByTypeIdAndLpuIdAndDateTimeAfter(Long reportTypeId, Integer lpuId,
			String dtFrom) {
		return reportDataRepository.findByTypeIdAndLpuIdAndDateTimeAfterOrderByDateTime(reportTypeId, lpuId,
				LocalDateTime.parse(dtFrom));
	}

	public Collection<ReportData> findByTypeIdAndDateTimeAfter(Long reportTypeId, String dtFrom) {
		return reportDataRepository.findByTypeIdAndDateTimeAfterOrderByDateTime(reportTypeId,
				LocalDateTime.parse(dtFrom));
	}

	public Collection<ReportData> findByTypeIdAndLpuIdAndDateTimeBefore(Long reportTypeId, Integer lpuId, String dtTo) {
		return reportDataRepository.findByTypeIdAndLpuIdAndDateTimeBeforeOrderByDateTime(reportTypeId, lpuId,
				LocalDateTime.parse(dtTo));
	}

	public Collection<ReportData> findByTypeIdAndDateTimeBefore(Long reportTypeId, String dtTo) {
		return reportDataRepository.findByTypeIdAndDateTimeBeforeOrderByDateTime(reportTypeId,
				LocalDateTime.parse(dtTo));
	}

	private Collection<ReportData> findByTypeIdAndLpuId(Long reportTypeId, Integer lpuId) {
		return reportDataRepository.findByTypeIdAndLpuId(reportTypeId, lpuId);
	}

	public Collection<ReportData> getDataByParams(FormParameters formParams) {
		Collection<ReportData> reportDatas = null;
		String timeTo = "T23:59:59";
		String timeFrom = "T00:00:00";
		if (formParams.getDtFrom() != null && !formParams.getDtFrom().isEmpty() && formParams.getDtTo() != null
				&& !formParams.getDtTo().isEmpty()) {

			if (formParams.getLpuId() != 0) {
				reportDatas = findByTypeIdAndLpuIdAndDateTimeBetween(formParams.getReportTypeId(),
						formParams.getLpuId(), formParams.getDtFrom() + timeFrom, formParams.getDtTo() + timeTo);
			} else {
				reportDatas = findByTypeIdAndDateTimeBetween(formParams.getReportTypeId(),
						formParams.getDtFrom() + timeFrom, formParams.getDtTo() + timeTo);
			}
		} else if (formParams.getDtFrom() != null && !formParams.getDtFrom().isEmpty()) {
			if (formParams.getLpuId() != 0) {
				reportDatas = findByTypeIdAndLpuIdAndDateTimeAfter(formParams.getReportTypeId(), formParams.getLpuId(),
						formParams.getDtFrom() + timeFrom);
			} else {
				reportDatas = findByTypeIdAndDateTimeAfter(formParams.getReportTypeId(),
						formParams.getDtFrom() + timeFrom);
			}
		} else if (formParams.getDtTo() != null && !formParams.getDtTo().isEmpty()) {
			if (formParams.getLpuId() != 0) {
				reportDatas = findByTypeIdAndLpuIdAndDateTimeBefore(formParams.getReportTypeId(), formParams.getLpuId(),
						formParams.getDtTo() + timeTo);
			} else {
				reportDatas = findByTypeIdAndDateTimeBefore(formParams.getReportTypeId(),
						formParams.getDtTo() + timeTo);
			}
		} else {
			if (formParams.getLpuId() != 0) {
				reportDatas = findByTypeIdAndLpuId(formParams.getReportTypeId(), formParams.getLpuId());
			} else {
				reportDatas = findByTypeId(formParams.getReportTypeId());
			}
		}

		return reportDatas;
	}

	public static Collection<Double> createSumRow(Collection<ReportData> reportDatas, ReportType reportType) {
		List<Double> result = new ArrayList<Double>();
		List<String> row;

		List<String> header = (List<String>) toCollection(reportType);
		for (ReportData reportData : reportDatas) {
			row = (List<String>) toCollection(reportData);
			for (int i = 0; i < row.size(); i++) {
				if (header.get(i).matches(".*[+]$") && row.get(i) != null) {
					Double oldVal = result.size() == i ? 0D : result.get(i);
					try {
						result.set(i, oldVal + Double.valueOf(row.get(i)));
					} catch (IndexOutOfBoundsException e) {
						result.add(Double.valueOf(row.get(i)));
					}
				} else {
					result.add(null);
				}
			}
		}

		return result;
	}

	public static Map<Integer, Map<LocalDateTime, Map<Integer, Collection<String>>>> createDataModel(
			Collection<ReportData> reportDatas) {
		if (reportDatas == null)
			return null;

		Map<Integer, Map<LocalDateTime, Map<Integer, Collection<String>>>> result = new LinkedHashMap<>();
		Map<LocalDateTime, Map<Integer, Collection<String>>> dateMap = new LinkedHashMap<>();
		Map<Integer, Collection<String>> rowMap = new LinkedHashMap<>();

		Set<Integer> lpuIds = reportDatas.stream().map(ReportData::getLpuId).collect(Collectors.toSet());
		for (Integer lpuId : lpuIds) {
			Set<LocalDateTime> dateTimes = reportDatas.stream()
					.filter(rd -> rd.getLpuId().intValue() == lpuId.intValue()).map(ReportData::getDateTime)
					.collect(Collectors.toSet());
			for (LocalDateTime dateTime : dateTimes) {
				Set<Integer> rowNums = reportDatas.stream().filter(rd -> rd.getLpuId().intValue() == lpuId.intValue())
						.filter(dt -> dt.getDateTime().equals(dateTime)).map(ReportData::getRowNum)
						.collect(Collectors.toSet());
				for (Integer rowNum : rowNums) {
					ReportData reportData = reportDatas.stream()
							.filter(rd -> rd.getLpuId().intValue() == lpuId.intValue())
							.filter(dt -> dt.getDateTime().equals(dateTime))
							.filter(rn -> rn.getRowNum().intValue() == rowNum.intValue()).findAny().get();
					rowMap.put(rowNum, toCollection(reportData));
				}
				dateMap.put(dateTime, rowMap);
			}
			result.put(lpuId, dateMap);
		}

		return result;
	}

	public static Collection<String> toCollection(MOReport report) {
		Collection<String> result = new ArrayList<String>();
		result.add(report.getE());
		result.add(report.getF());
		result.add(report.getG());
		result.add(report.getH());
		result.add(report.getI());
		result.add(report.getJ());
		result.add(report.getK());
		result.add(report.getL());
		result.add(report.getM());
		result.add(report.getN());
		result.add(report.getO());
		result.add(report.getP());
		result.add(report.getQ());
		result.add(report.getR());
		result.add(report.getS());
		result.add(report.getT());
		result.add(report.getU());
		result.add(report.getV());
		result.add(report.getW());
		result.add(report.getX());
		result.add(report.getY());
		result.add(report.getZ());
		result.add(report.getAa());
		result.add(report.getAb());
		result.add(report.getAc());
		result.add(report.getAd());
		result.add(report.getAe());
		result.add(report.getAf());
		result.add(report.getAg());
		result.add(report.getAh());
		result.add(report.getAi());
		result.add(report.getAj());
		result.add(report.getAk());
		result.add(report.getAl());
		result.add(report.getAm());
		result.add(report.getAn());
		result.add(report.getAo());
		result.add(report.getAp());
		result.add(report.getAq());
		result.add(report.getAr());
		result.add(report.getAs());
		result.add(report.getAt());
		result.add(report.getAu());
		result.add(report.getAv());
		result.add(report.getAw());
		result.add(report.getAx());
		result.add(report.getAy());
		result.add(report.getAz());
		result.add(report.getBa());
		result.add(report.getBb());

		return result;
	}

	public InputStream createExcel(ReportType reportType, Lpu lpu, FormParameters formParams) throws IOException {
		return new ExcelReportGenerator(reportType, lpu).createBody(createDataModel(getDataByParams(formParams)))
				.createSumRow(createSumRow(getDataByParams(formParams), reportType)).toExcel();
	}

	public Page<ReportData> findByLpuId(Integer lpuId, Optional<Integer> page) {
		int currentPage = page.orElse(1);
		PageRequest pageRequest = PageRequest.of(currentPage - 1, PAGE_SIZE);

		return reportDataRepository.findByLpuIdOrderByDateTime(lpuId, pageRequest);
	}

	public InputStream getReport(Long typeId, LocalDateTime dateTime, Integer lpuId) {
		ReportData report = reportDataRepository.findByTypeIdAndLpuIdAndDateTime(typeId,lpuId,dateTime).stream().findAny().get();
		byte[] reportFile = null;
		if (report != null) {
			reportFile = report.getFile();
		} else {
			throw new NullPointerException();
		}
		
		return new ByteArrayInputStream(reportFile);
	}
}
