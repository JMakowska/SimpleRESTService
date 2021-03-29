package com.rest.service.simpleRESTService.repository;

import com.rest.service.simpleRESTService.entity.UserCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCounterRepository extends JpaRepository<UserCounter, String> {
}
