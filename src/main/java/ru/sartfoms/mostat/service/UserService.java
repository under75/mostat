package ru.sartfoms.mostat.service;

import org.springframework.stereotype.Service;

import ru.sartfoms.mostat.entity.User;
import ru.sartfoms.mostat.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository repository;
	
	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	public User getByName(String name) {
		return repository.getReferenceById(name);
	}
}
