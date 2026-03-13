package com.example.hrbank.domain.department.service;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.request.DepartmentCreateRequest;
import java.util.List;

public interface DepartmentService {
  //부서 수정
  //부서 삭제

  DepartmentDto create(DepartmentCreateRequest departmentCreateRequest);
  DepartmentDto find(Long DepartmentId);
  List<DepartmentDto> findAll();
//  DepartmentDto update(Long DepartmentId, )
}
