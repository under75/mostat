package ru.sartfoms.mostat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.ReportStatus;
import ru.sartfoms.mostat.entity.ReportStatusId;

public interface ReportStatusRepository extends JpaRepository<ReportStatus, ReportStatusId> {

	Page<ReportStatus> findByLpuIdAndStatus(Integer id, int status, PageRequest pageRequest);

	Page<ReportStatus> findByLpuIdAndStatusNot(Integer lpuId, int status, PageRequest pageRequest);

}
