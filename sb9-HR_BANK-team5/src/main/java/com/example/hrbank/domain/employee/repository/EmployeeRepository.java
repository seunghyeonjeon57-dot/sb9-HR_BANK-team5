package com.example.hrbank.domain.employee.repository;


import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.repository.custom.EmployeeRepositoryCustom;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>, EmployeeRepositoryCustom {
  Stream<Employee> streamAllBy();
  @EntityGraph(attributePaths = {"department","profileImage"})
  Optional<Employee> findWithDetailsById(Long Id);
  boolean existsByEmail(String email);
  Optional<Employee> findByEmployeeNumber(String employeeNumber);

}