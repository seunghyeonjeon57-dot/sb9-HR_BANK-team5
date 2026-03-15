package com.example.hrbank.domain.employee.repository.custom;

import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import com.example.hrbank.domain.employee.entity.Employee;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepositoryCustom {
  public List<Employee> searchEmployee(EmployeeSearchRequest request);
  public Long countEmployee(EmployeeSearchRequest request);
  public Employee updateEmployee(EmployeeUpdateRequest request);

}
