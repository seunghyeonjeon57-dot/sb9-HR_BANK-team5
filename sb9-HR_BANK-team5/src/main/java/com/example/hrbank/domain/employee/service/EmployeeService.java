package com.example.hrbank.domain.employee.service;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.dto.request.EmployeeCreateRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
  EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profile);
  CursorPageResponseEmployeeDto searchEmployees(EmployeeSearchRequest request);
  EmployeeDto searchEmployeeById(Long id);
  EmployeeDto updateEmployee(Long id,EmployeeUpdateRequest updateRequest, MultipartFile profile,String ipAddress);
  void deleteEmployee(Long id, String ipAddress);


}
