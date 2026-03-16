package com.example.hrbank.domain.department.repository.custom;

import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.entity.Department;
import java.util.List;

public interface DepartmentRepositoryCustom {
  List<Department> searchDepartments(DepartmentSearchRequest request);

  Long countDepartments(DepartmentSearchRequest request);

}
