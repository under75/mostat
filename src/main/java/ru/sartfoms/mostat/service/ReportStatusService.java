package ru.sartfoms.mostat.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.entity.ReportStatus;
import ru.sartfoms.mostat.entity.ReportStatusId;
import ru.sartfoms.mostat.model.FormParameters;
import ru.sartfoms.mostat.repository.LpuRepository;
import ru.sartfoms.mostat.repository.ReportStatusRepository;
import ru.sartfoms.mostat.service.util.Status;

@Service
public class ReportStatusService {
	private static final Integer PAGE_SIZE = 10;
	private final ReportStatusRepository reportStatusRepository;
	private final LpuRepository lpuRepository;

	public ReportStatusService(ReportStatusRepository reportStatusRepository, LpuRepository lpuRepository) {
		this.reportStatusRepository = reportStatusRepository;
		this.lpuRepository = lpuRepository;
	}

	public void save(FormParameters formParams) {
		Collection<Lpu> lpus;
		Collection<ReportStatus> statuses = new ArrayList<>();
		if (formParams.getLpuId().intValue() == 0) {
			lpus = lpuRepository.findAll();
		} else {
			lpus = new ArrayList<>();
			lpus.add(lpuRepository.getReferenceById(formParams.getLpuId()));
		}
		int nr = 0;
		for (Iterator<Lpu> iterator = lpus.iterator(); iterator.hasNext();) {
			Lpu lpu = (Lpu) iterator.next();
			ReportStatus entity = new ReportStatus();
			entity.setTypeId(formParams.getReportTypeId());
			entity.setLpuId(lpu.getId());
			entity.setStatus(Status.OPEN);
			entity.setDtRep(LocalDate.parse(formParams.getDtRep()));
			entity.setUsrIns(SecurityContextHolder.getContext().getAuthentication().getName());
			entity.setDtIns(LocalDate.now());
			statuses.add(entity);

			if (++nr % 1000 == 0 || !iterator.hasNext()) {
				reportStatusRepository.saveAll(statuses);
				statuses.clear();
			}
		}
	}

	public Page<ReportStatus> findAll(Optional<Integer> page) {
		int currentPage = page.orElse(1);
		PageRequest pageRequest = PageRequest.of(currentPage - 1, PAGE_SIZE,
				Sort.by("dtRep").descending().and(Sort.by("typeId")).and(Sort.by("lpuId")));

		return reportStatusRepository.findAll(pageRequest);
	}

	public Page<ReportStatus> findByLpuIdAndStatus(Integer id, int status, Optional<Integer> page) {
		int currentPage = page.orElse(1);
		PageRequest pageRequest = PageRequest.of(currentPage - 1, PAGE_SIZE,
				Sort.by("dtRep").ascending().and(Sort.by("typeId")));

		return reportStatusRepository.findByLpuIdAndStatus(id, status, pageRequest);
	}

	public Page<ReportStatus> findByLpuIdAndStatusNot(Integer lpuId, int status, Optional<Integer> page) {
		int currentPage = page.orElse(1);
		PageRequest pageRequest = PageRequest.of(currentPage - 1, PAGE_SIZE,
				Sort.by("dtRep").descending().and(Sort.by("typeId")));

		return reportStatusRepository.findByLpuIdAndStatusNot(lpuId, status, pageRequest);
	}

	public void closeReport(Long typeId, Integer lpuId, String dtRep) {
		ReportStatus reportStatus = reportStatusRepository
				.getReferenceById(new ReportStatusId(typeId, lpuId, LocalDate.parse(dtRep)));
		reportStatus.setStatus(Status.CLOSED);
		
		reportStatusRepository.save(reportStatus);
	}

}
