package com.example.hrbank.domain.department.repository.impl;

import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.entity.QDepartment;
import com.example.hrbank.domain.department.repository.custom.DepartmentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;

public class DepartmentRepositoryImpl implements DepartmentRepositoryCustom {
  private final JPAQueryFactory queryFactory;
  private final QDepartment department = QDepartment.department;

  public DepartmentRepositoryImpl(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Override
  public List<Department> searchDepartments(DepartmentSearchRequest request) {
    return List.of();
  }

  @Override
  public Long countDepartments(DepartmentSearchRequest request) {
    return 0L;
  }
}
