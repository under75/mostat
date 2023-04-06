package ru.sartfoms.mostat.service;

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

	public ReportTypeService(ReportTypeRepository reportTypeRepository) {
		this.reportTypeRepository = reportTypeRepository;
	}

	public Page<ReportType> findAll(Optional<Integer> page) {
		int currentPage = page.orElse(1);
		PageRequest pageRequest = PageRequest.of(currentPage - 1, PAGE_SIZE);
		
		return reportTypeRepository.findAll(pageRequest);
	}
}
