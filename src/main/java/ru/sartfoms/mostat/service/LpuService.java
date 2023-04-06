package ru.sartfoms.mostat.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportType;
import ru.sartfoms.mostat.repository.LpuRepository;
import ru.sartfoms.mostat.repository.ReportTypeRepository;

@Service
public class LpuService {
	private final LpuRepository lpuRepository;
	private final ReportTypeRepository reportTypeRepository;

	public LpuService(LpuRepository lpuRepository, ReportTypeRepository reportTypeRepository) {
		this.lpuRepository = lpuRepository;
		this.reportTypeRepository = reportTypeRepository;
	}

	public Lpu getById(Integer id) {
		return lpuRepository.getReferenceById(id);
	}

	public InputStream createExcel(Long id) throws IOException  {
		ReportType reportType = reportTypeRepository.getReferenceById(id);
		
		return new ExcelTemplateGenerator(reportType).toExcel();
	}
}
