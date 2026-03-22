package com.example.hrbank.domain.employee.repository;


import com.example.hrbank.domain.employee.entity.Employee;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>, EmployeeRepositoryCustom {
  Stream<Employee> streamAllBy();
  @EntityGraph(attributePaths = {"department","profileImage"})
  Optional<Employee> findById(Long id);
  @Query(value = "SELECT employee_number FROM employees WHERE employee_number LIKE 'EMP-2026%' ORDER BY employee_number DESC LIMIT 1", nativeQuery = true)
  Optional<String> findLastEmployeeNumber();
  boolean existsByEmail(String email);
  Optional<Employee> findByEmployeeNumber(String employeeNumber);
  boolean existsByDepartmentId(Long id);

}