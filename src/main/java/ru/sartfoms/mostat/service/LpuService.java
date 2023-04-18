package ru.sartfoms.mostat.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportType;
import ru.sartfoms.mostat.entity.User;
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

	public InputStream createExcel(Long id, User user) throws IOException  {
		ReportType reportType = reportTypeRepository.getReferenceById(id);
		Lpu lpu = getById(user.getLpuId());
		
		return new ExcelTemplateGenerator(reportType, lpu).toExcel();
	}

	public Collection<Lpu> findByIdIn(Set<Integer> ids) {
		return lpuRepository.findByIdIn(ids);
	}

	public Lpu getDummy() {
		Lpu dummy = new Lpu();
		dummy.setId(0);
		dummy.setName("Все МО");
		
		return dummy;
	}
}
