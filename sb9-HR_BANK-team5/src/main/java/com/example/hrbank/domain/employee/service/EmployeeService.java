package com.example.hrbank.domain.employee.service;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.dto.request.EmployeeCreateRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import java.util.List;

public interface EmployeeService {
  EmployeeDto create(EmployeeCreateRequest request, BinaryContent profile);
  CursorPageResponseEmployeeDto getEmployeePage(EmployeeSearchRequest request);
  EmployeeDto findEmployeeById(Long id);
  EmployeeDto updateEmployee(Long id,EmployeeUpdateRequest updateRequest);
  void deleteEmployee(Long id);


}
