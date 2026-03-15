package com.example.hrbank.domain.department.dto.response;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.entity.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.reflect.Array;
import java.util.List;

@Schema(description = "커서 기반 페이지 응답")
public record CursorPageResponseDepartmentDto(
  List<DepartmentDto> content,
  String nextCursor,
  Long nextIdAfter,
  Integer size,
  Long totalElements,
  boolean hasNext
) {}
