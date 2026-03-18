package com.example.hrbank.domain.employee.repository;

import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.entity.Employee;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepositoryCustom {
  public List<Employee> totalEmployee(EmployeeSearchRequest request,String lastValue,Long lastId);
  public Long totalCountEmployee(EmployeeSearchRequest request);


}