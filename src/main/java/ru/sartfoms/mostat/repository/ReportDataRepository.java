package ru.sartfoms.mostat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.ReportData;

public interface ReportDataRepository extends JpaRepository<ReportData, Long> {

}
