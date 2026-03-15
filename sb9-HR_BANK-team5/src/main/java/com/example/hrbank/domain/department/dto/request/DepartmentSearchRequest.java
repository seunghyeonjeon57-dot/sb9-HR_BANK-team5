package com.example.hrbank.domain.department.dto.request;

//이 요청은 스웨거api에는 없지만 DepartmentService에서 findAll메서드의
//파라미터에 너무 많은 값을 넣어야 해서 이걸 만듬
public record DepartmentSearchRequest(
    String nameOrDescription,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection
) {}
