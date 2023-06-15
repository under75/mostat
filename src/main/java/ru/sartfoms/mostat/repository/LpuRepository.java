package ru.sartfoms.mostat.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.Lpu;

public interface LpuRepository extends JpaRepository<Lpu, Integer> {

	Collection<Lpu> findByIdIn(Set<Integer> ids);

	Collection<? extends Lpu> findAllByOrderById();

}
