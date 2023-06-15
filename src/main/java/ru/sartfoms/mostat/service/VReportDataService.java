package ru.sartfoms.mostat.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportType;
import ru.sartfoms.mostat.entity.VReportData;
import ru.sartfoms.mostat.model.FormParameters;
import ru.sartfoms.mostat.model.MOReport;
import ru.sartfoms.mostat.repository.VReportDataRepository;

@Service
public class VReportDataService {
	private final VReportDataRepository vReportDataRepository;

	public VReportDataService(VReportDataRepository vReportDataRepository) {
		this.vReportDataRepository = vReportDataRepository;
	}

	public Collection<VReportData> findByTypeId(Long reportTypeId) {
		return vReportDataRepository.findByTypeId(reportTypeId);
	}

	public Collection<VReportData> getDataByParams(FormParameters formParams) {
		Collection<VReportData> reportDatas = null;
		if (formParams.getDtFrom() != null && !formParams.getDtFrom().isEmpty() && formParams.getDtTo() != null
				&& !formParams.getDtTo().isEmpty()) {

			if (formParams.getLpuId() != 0) {
				reportDatas = findByTypeIdAndLpuIdAndDtRepBetween(formParams.getReportTypeId(), formParams.getLpuId(),
						formParams.getDtFrom(), formParams.getDtTo());
			} else {
				reportDatas = findByTypeIdAndDtRepBetween(formParams.getReportTypeId(), formParams.getDtFrom(),
						formParams.getDtTo());
			}
		} else if (formParams.getDtFrom() != null && !formParams.getDtFrom().isEmpty()) {
			if (formParams.getLpuId() != 0) {
				reportDatas = findByTypeIdAndLpuIdAndDtRepAfter(formParams.getReportTypeId(), formParams.getLpuId(),
						formParams.getDtFrom());
			} else {
				reportDatas = findByTypeIdAndDtRepAfter(formParams.getReportTypeId(), formParams.getDtFrom());
			}
		} else if (formParams.getDtTo() != null && !formParams.getDtTo().isEmpty()) {
			if (formParams.getLpuId() != 0) {
				reportDatas = findByTypeIdAndLpuIdAndDtRepBefore(formParams.getReportTypeId(), formParams.getLpuId(),
						formParams.getDtTo());
			} else {
				reportDatas = findByTypeIdAndDtRepBefore(formParams.getReportTypeId(), formParams.getDtTo());
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

	public Collection<VReportData> findByTypeIdAndLpuIdAndDtRepBetween(Long reportTypeId, Integer lpuId, String dtFrom,
			String dtTo) {
		return vReportDataRepository.findByTypeIdAndLpuIdAndDtRepBetweenOrderByDtRep(reportTypeId, lpuId,
				LocalDate.parse(dtFrom), LocalDate.parse(dtTo));
	}

	public Collection<VReportData> findByTypeIdAndDtRepBetween(Long reportTypeId, String dtFrom, String dtTo) {
		return vReportDataRepository.findByTypeIdAndDtRepBetweenOrderByDtRep(reportTypeId, LocalDate.parse(dtFrom),
				LocalDate.parse(dtTo));
	}

	public Collection<VReportData> findByTypeIdAndLpuIdAndDtRepAfter(Long reportTypeId, Integer lpuId, String dtFrom) {
		return vReportDataRepository.findByTypeIdAndLpuIdAndDtRepAfterOrderByDtRep(reportTypeId, lpuId,
				LocalDate.parse(dtFrom).minusDays(1));
	}

	public Collection<VReportData> findByTypeIdAndDtRepAfter(Long reportTypeId, String dtFrom) {
		return vReportDataRepository.findByTypeIdAndDtRepAfterOrderByDtRep(reportTypeId,
				LocalDate.parse(dtFrom).minusDays(1));
	}

	public Collection<VReportData> findByTypeIdAndLpuIdAndDtRepBefore(Long reportTypeId, Integer lpuId, String dtTo) {
		return vReportDataRepository.findByTypeIdAndLpuIdAndDtRepBeforeOrderByDtRep(reportTypeId, lpuId,
				LocalDate.parse(dtTo).plusDays(1));
	}

	public Collection<VReportData> findByTypeIdAndDtRepBefore(Long reportTypeId, String dtTo) {
		return vReportDataRepository.findByTypeIdAndDtRepBeforeOrderByDtRep(reportTypeId,
				LocalDate.parse(dtTo).plusDays(1));
	}

	private Collection<VReportData> findByTypeIdAndLpuId(Long reportTypeId, Integer lpuId) {
		return vReportDataRepository.findByTypeIdAndLpuId(reportTypeId, lpuId);
	}

	public static Map<Integer, Map<LocalDate, Map<Integer, Collection<String>>>> createDataModel(
			Collection<VReportData> reportDatas) {
		if (reportDatas == null)
			return null;

		Map<Integer, Map<LocalDate, Map<Integer, Collection<String>>>> result = new LinkedHashMap<>();

		Set<Integer> lpuIds = reportDatas.stream().map(VReportData::getLpuId).collect(Collectors.toCollection(LinkedHashSet::new));
		for (Integer lpuId : lpuIds) {
			NavigableSet<LocalDate> dtReps = new TreeSet<>(Comparator.reverseOrder());
			dtReps.addAll(reportDatas.stream().filter(rd -> rd.getLpuId().intValue() == lpuId.intValue())
					.map(VReportData::getDtRep).collect(Collectors.toSet()));
			Map<LocalDate, Map<Integer, Collection<String>>> dateMap = new LinkedHashMap<>();
			for (LocalDate dtRep : dtReps) {
				Set<Integer> rowNums = reportDatas.stream().filter(rd -> rd.getLpuId().intValue() == lpuId.intValue())
						.filter(dt -> dt.getDtRep().equals(dtRep)).map(VReportData::getRowNum)
						.collect(Collectors.toSet());
				Map<Integer, Collection<String>> rowMap = new LinkedHashMap<>();
				for (Integer rowNum : rowNums) {
					VReportData reportData = reportDatas.stream()
							.filter(rd -> rd.getLpuId().intValue() == lpuId.intValue())
							.filter(dt -> dt.getDtRep().equals(dtRep))
							.filter(rn -> rn.getRowNum().intValue() == rowNum.intValue()).findAny().get();
					rowMap.put(rowNum, toCollection(reportData));
				}
				dateMap.put(dtRep, rowMap);
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

	public static Collection<Double> createSumRow(Collection<VReportData> reportDatas, ReportType reportType) {
		List<Double> result = new ArrayList<>();
		List<String> row;

		List<String> header = (List<String>) toCollection(reportType);
		// init result
		for (int i = 0; i < header.size(); i++) {
			result.add(null);
		}
		for (VReportData reportData : reportDatas) {
			row = (List<String>) toCollection(reportData);
			for (int i = 0; i < row.size(); i++) {
				// summation sign
				if (header.get(i) != null && header.get(i).trim().matches(".*[+]$") && row.get(i) != null) {
					Double oldVal = result.get(i);
					if (oldVal == null) {
						result.set(i, new BigDecimal(Double.valueOf(row.get(i))).setScale(2, RoundingMode.HALF_UP)
								.doubleValue());
					} else {
						result.set(i, new BigDecimal(oldVal + Double.valueOf(row.get(i)))
								.setScale(2, RoundingMode.HALF_UP).doubleValue());
					}
				}
			}
		}

		return result;
	}

	public InputStream createExcel(ReportType reportType, Lpu lpu, FormParameters formParams) throws IOException {
		return new ExcelReportGenerator(reportType, lpu).createBody(createDataModel(getDataByParams(formParams)))
				.createSumRow(createSumRow(getDataByParams(formParams), reportType)).toExcel();
	}
}
