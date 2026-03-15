package com.example.hrbank.domain.department.repository;

import com.example.hrbank.domain.department.entity.Department;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

  boolean existsByName(String name);

  Optional<Department> findById(Long id);
}
