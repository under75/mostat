package ru.sartfoms.mostat.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.sartfoms.mostat.entity.ReportType;
import ru.sartfoms.mostat.repository.ReportTypeRepository;

@Service
public class ReportTypeService {
	private static final Integer PAGE_SIZE = 10;
	private final ReportTypeRepository reportTypeRepository;
	protected final static String HEADER_DELIMETER = "::";

	public ReportTypeService(ReportTypeRepository reportTypeRepository) {
		this.reportTypeRepository = reportTypeRepository;
	}

	public Page<ReportType> findAll(Optional<Integer> page) {
		int currentPage = page.orElse(1);
		PageRequest pageRequest = PageRequest.of(currentPage - 1, PAGE_SIZE);
		
		return reportTypeRepository.findAll(pageRequest);
	}

	public Collection<ReportType> findAll() {
		return reportTypeRepository.findAll();
	}

	public ReportType getById(Long reportTypeId) {
		return reportTypeRepository.getReferenceById(reportTypeId);
	}
	
	public static Map<String, Collection<String>> createHeaderModel(ReportType reportType) {
		Map<String, Collection<String>> model = new LinkedHashMap<>();
		buildModel(reportType.getE(), "", model);
		buildModel(reportType.getF(), reportType.getE(), model);
		buildModel(reportType.getG(), reportType.getF(), model);
		buildModel(reportType.getH(), reportType.getG(), model);
		buildModel(reportType.getI(), reportType.getH(), model);
		buildModel(reportType.getJ(), reportType.getI(), model);
		buildModel(reportType.getK(), reportType.getJ(), model);
		buildModel(reportType.getL(), reportType.getK(), model);
		buildModel(reportType.getM(), reportType.getL(), model);
		buildModel(reportType.getN(), reportType.getM(), model);
		buildModel(reportType.getO(), reportType.getN(), model);
		buildModel(reportType.getP(), reportType.getO(), model);
		buildModel(reportType.getQ(), reportType.getP(), model);
		buildModel(reportType.getR(), reportType.getQ(), model);
		buildModel(reportType.getS(), reportType.getR(), model);
		buildModel(reportType.getT(), reportType.getS(), model);
		buildModel(reportType.getU(), reportType.getT(), model);
		buildModel(reportType.getV(), reportType.getU(), model);
		buildModel(reportType.getW(), reportType.getV(), model);
		buildModel(reportType.getX(), reportType.getW(), model);
		buildModel(reportType.getY(), reportType.getX(), model);
		buildModel(reportType.getZ(), reportType.getY(), model);
		buildModel(reportType.getAa(), reportType.getZ(), model);
		buildModel(reportType.getAb(), reportType.getAa(), model);
		buildModel(reportType.getAc(), reportType.getAb(), model);
		buildModel(reportType.getAd(), reportType.getAc(), model);
		buildModel(reportType.getAe(), reportType.getAd(), model);
		buildModel(reportType.getAf(), reportType.getAe(), model);
		buildModel(reportType.getAg(), reportType.getAf(), model);
		buildModel(reportType.getAh(), reportType.getAg(), model);
		buildModel(reportType.getAi(), reportType.getAh(), model);
		buildModel(reportType.getAj(), reportType.getAi(), model);
		buildModel(reportType.getAk(), reportType.getAj(), model);
		buildModel(reportType.getAl(), reportType.getAk(), model);
		buildModel(reportType.getAm(), reportType.getAl(), model);
		buildModel(reportType.getAn(), reportType.getAm(), model);
		buildModel(reportType.getAo(), reportType.getAn(), model);
		buildModel(reportType.getAp(), reportType.getAo(), model);
		buildModel(reportType.getAq(), reportType.getAp(), model);
		buildModel(reportType.getAr(), reportType.getAq(), model);
		buildModel(reportType.getAs(), reportType.getAr(), model);
		buildModel(reportType.getAt(), reportType.getAs(), model);
		buildModel(reportType.getAu(), reportType.getAt(), model);
		buildModel(reportType.getAv(), reportType.getAu(), model);
		buildModel(reportType.getAw(), reportType.getAv(), model);
		buildModel(reportType.getAx(), reportType.getAw(), model);
		buildModel(reportType.getAy(), reportType.getAx(), model);
		buildModel(reportType.getAz(), reportType.getAy(), model);
		buildModel(reportType.getBa(), reportType.getAz(), model);
		buildModel(reportType.getBb(), reportType.getBa(), model);

		return model;
	}

	private static void buildModel(String cellValue, String prevValue, Map<String, Collection<String>> model) {
		if (cellValue.contains(HEADER_DELIMETER)) {
			if (cellValue.split(HEADER_DELIMETER)[0].equals(prevValue.split(HEADER_DELIMETER)[0])) {
				model.get(cellValue.split(HEADER_DELIMETER)[0]).add(cellValue.split(HEADER_DELIMETER)[1]);
			} else {
				Collection<String> block = new ArrayList<>();
				block.add(cellValue.split(HEADER_DELIMETER)[1]);
				model.put(cellValue.split(HEADER_DELIMETER)[0], block);
			}
		} else {
			model.put(cellValue, null);
		}

	}
}
