package ru.sartfoms.mostat.service;

import org.springframework.stereotype.Service;

import ru.sartfoms.mostat.entity.Lpu;
import ru.sartfoms.mostat.repository.LpuRepository;

@Service
public class LpuService {
	private final LpuRepository lpuRepository;

	public LpuService(LpuRepository lpuRepository) {
		this.lpuRepository = lpuRepository;
	}

	public Lpu getById(Integer id) {
		return lpuRepository.getReferenceById(id);
	}
}
