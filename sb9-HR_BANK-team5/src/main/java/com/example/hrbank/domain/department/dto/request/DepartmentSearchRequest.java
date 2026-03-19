package com.example.hrbank.domain.department.dto.request;


public record DepartmentSearchRequest(
    String nameOrDescription,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection
) {}
