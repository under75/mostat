package ru.sartfoms.mostat.repository;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.ReportDataId;
import ru.sartfoms.mostat.entity.VReportData;

public interface VReportDataRepository extends JpaRepository<VReportData, ReportDataId> {
	Collection<VReportData> findByTypeId(Long reportTypeId);

	Collection<VReportData> findByTypeIdAndLpuIdAndDtRepBetweenOrderByDtRep(Long reportTypeId, Integer lpuId,
			LocalDate dtFrom, LocalDate dtTo);

	Collection<VReportData> findByTypeIdAndDtRepBetweenOrderByDtRep(Long reportTypeId, LocalDate parse,
			LocalDate parse2);

	Collection<VReportData> findByTypeIdAndLpuIdAndDtRepAfterOrderByDtRep(Long reportTypeId, Integer lpuId,
			LocalDate parse);

	Collection<VReportData> findByTypeIdAndDtRepAfterOrderByDtRep(Long reportTypeId, LocalDate parse);

	Collection<VReportData> findByTypeIdAndLpuIdAndDtRepBeforeOrderByDtRep(Long reportTypeId, Integer lpuId,
			LocalDate parse);

	Collection<VReportData> findByTypeIdAndDtRepBeforeOrderByDtRep(Long reportTypeId, LocalDate parse);

	Collection<VReportData> findByTypeIdAndLpuId(Long reportTypeId, Integer lpuId);

	Collection<VReportData> findByTypeIdAndLpuIdAndDtRep(Long typeId, Integer lpuId, LocalDate dtRep);

	Page<VReportData> findByLpuIdAndRowNumOrderByDtRepDesc(Integer lpuId, int i, PageRequest pageRequest);

	VReportData findByTypeIdAndDtRep(Long typeId, LocalDate parse);
}
