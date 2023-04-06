package ru.sartfoms.mostat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.ReportType;

public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {

}
