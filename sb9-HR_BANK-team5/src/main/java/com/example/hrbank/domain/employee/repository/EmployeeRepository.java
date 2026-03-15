package com.example.hrbank.domain.employee.repository;

import com.example.hrbank.domain.employee.entity.Employee;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
  Stream<Employee> streamAllBy();
  Optional<Employee> findByEmployeeNumber(String employeeNumber);

}
