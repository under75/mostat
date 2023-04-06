package ru.sartfoms.mostat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.sartfoms.mostat.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
