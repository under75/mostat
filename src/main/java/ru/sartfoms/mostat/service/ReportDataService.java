package ru.sartfoms.mostat.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.sartfoms.mostat.entity.ReportData;
import ru.sartfoms.mostat.entity.User;
import ru.sartfoms.mostat.model.FormParameters;
import ru.sartfoms.mostat.repository.ReportDataRepository;

@Service
public class ReportDataService {
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
					parser.write(reportData);

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

	public static Collection<String> toCollection(ReportData reportData) {
		Collection<String> result = new ArrayList<String>();
		result.add(reportData.getE());
		result.add(reportData.getF());
		result.add(reportData.getG());
		result.add(reportData.getH());
		result.add(reportData.getI());
		result.add(reportData.getJ());
		result.add(reportData.getK());
		result.add(reportData.getL());
		result.add(reportData.getM());
		result.add(reportData.getN());
		result.add(reportData.getO());
		result.add(reportData.getP());
		result.add(reportData.getQ());
		result.add(reportData.getR());
		result.add(reportData.getS());
		result.add(reportData.getT());
		result.add(reportData.getU());
		result.add(reportData.getV());
		result.add(reportData.getW());
		result.add(reportData.getX());
		result.add(reportData.getY());
		result.add(reportData.getZ());
		result.add(reportData.getAa());
		result.add(reportData.getAb());
		result.add(reportData.getAc());
		result.add(reportData.getAd());
		result.add(reportData.getAe());
		result.add(reportData.getAf());
		result.add(reportData.getAg());
		result.add(reportData.getAh());
		result.add(reportData.getAi());
		result.add(reportData.getAj());
		result.add(reportData.getAk());
		result.add(reportData.getAl());
		result.add(reportData.getAm());
		result.add(reportData.getAn());
		result.add(reportData.getAo());
		result.add(reportData.getAp());
		result.add(reportData.getAq());
		result.add(reportData.getAr());
		result.add(reportData.getAs());
		result.add(reportData.getAt());
		result.add(reportData.getAu());
		result.add(reportData.getAv());
		result.add(reportData.getAw());
		result.add(reportData.getAx());
		result.add(reportData.getAy());
		result.add(reportData.getAz());
		result.add(reportData.getBa());
		result.add(reportData.getBb());

		return result;
	}
}
