package com.example.hrbank.domain.employee.service.Impl;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.dto.request.EmployeeCreateRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import com.example.hrbank.domain.employee.service.EmployeeService;

public class EmployeeServiceImpl implements EmployeeService {

  @Override
  public EmployeeDto create(EmployeeCreateRequest request, BinaryContent profile) {
    return null;
  }

  @Override
  public CursorPageResponseEmployeeDto getEmployeePage(EmployeeSearchRequest request) {
    return null;
  }

  @Override
  public EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest updateRequest) {
    return null;
  }

  @Override
  public EmployeeDto findEmployeeById(EmployeeDto dto) {
    return null;
  }

  @Override
  public void deleteEmployee(Long id) {

  }
}
