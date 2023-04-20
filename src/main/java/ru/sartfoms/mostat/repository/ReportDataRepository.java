package ru.sartfoms.mostat.repository;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.ReportData;
import ru.sartfoms.mostat.entity.ReportDataId;

public interface ReportDataRepository extends JpaRepository<ReportData, ReportDataId> {

	Collection<ReportData> findByTypeId(Long reportTypeId);

	Collection<ReportData> findByTypeIdAndLpuIdAndDateTimeBetweenOrderByDateTime(Long reportTypeId, Integer lpuId,
			LocalDateTime dtFrom, LocalDateTime dtTo);

	Collection<ReportData> findByTypeIdAndDateTimeBetweenOrderByDateTime(Long reportTypeId, LocalDateTime parse,
			LocalDateTime parse2);

	Collection<ReportData> findByTypeIdAndLpuIdAndDateTimeAfterOrderByDateTime(Long reportTypeId, Integer lpuId,
			LocalDateTime parse);

	Collection<ReportData> findByTypeIdAndDateTimeAfterOrderByDateTime(Long reportTypeId, LocalDateTime parse);

	Collection<ReportData> findByTypeIdAndLpuIdAndDateTimeBeforeOrderByDateTime(Long reportTypeId, Integer lpuId,
			LocalDateTime parse);

	Collection<ReportData> findByTypeIdAndDateTimeBeforeOrderByDateTime(Long reportTypeId, LocalDateTime parse);

	Collection<ReportData> findByTypeIdAndLpuId(Long reportTypeId, Integer lpuId);

	Page<ReportData> findByLpuIdOrderByDateTime(Integer lpuId, PageRequest pageRequest);

	Collection<ReportData> findByTypeIdAndLpuIdAndDateTime(Long typeId, Integer lpuId, LocalDateTime dateTime);

}
