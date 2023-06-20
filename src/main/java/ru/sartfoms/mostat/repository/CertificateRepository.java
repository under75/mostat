package ru.sartfoms.mostat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

}
