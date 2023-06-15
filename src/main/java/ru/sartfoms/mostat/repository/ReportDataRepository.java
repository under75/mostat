package ru.sartfoms.mostat.repository;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.ReportData;
import ru.sartfoms.mostat.entity.ReportDataId;

public interface ReportDataRepository extends JpaRepository<ReportData, ReportDataId> {

	Collection<ReportData> findByTypeId(Long reportTypeId);

	Collection<ReportData> findByTypeIdAndLpuIdAndDtRepBetweenOrderByDtRep(Long reportTypeId, Integer lpuId,
			LocalDate dtFrom, LocalDate dtTo);

	Collection<ReportData> findByTypeIdAndDtRepBetweenOrderByDtRep(Long reportTypeId, LocalDate parse,
			LocalDate parse2);

	Collection<ReportData> findByTypeIdAndLpuIdAndDtRepAfterOrderByDtRep(Long reportTypeId, Integer lpuId,
			LocalDate parse);

	Collection<ReportData> findByTypeIdAndDtRepAfterOrderByDtRep(Long reportTypeId, LocalDate parse);

	Collection<ReportData> findByTypeIdAndLpuIdAndDtRepBeforeOrderByDtRep(Long reportTypeId, Integer lpuId,
			LocalDate parse);

	Collection<ReportData> findByTypeIdAndDtRepBeforeOrderByDtRep(Long reportTypeId, LocalDate parse);

	Collection<ReportData> findByTypeIdAndLpuId(Long reportTypeId, Integer lpuId);

	Collection<ReportData> findByTypeIdAndLpuIdAndDtRep(Long typeId, Integer lpuId, LocalDate dtRep);

	Page<ReportData> findByLpuIdAndRowNumOrderByDtRepDesc(Integer lpuId, int i, PageRequest pageRequest);

	ReportData findByTypeIdAndDtRep(Long typeId, LocalDate parse);

}
