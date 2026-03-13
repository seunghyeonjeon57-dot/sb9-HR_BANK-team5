package com.example.hrbank.domain.department.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.reflect.Array;

@Schema(description = "커서 기반 페이지 응답")
public record CursorPageResponseDepartmentDto(
  Array content,
  String nextCursor,
  Integer nextIdAfter,
  Integer size,
  Integer totalElements,
  boolean hasNext
) {}
